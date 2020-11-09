package fr.ifpen.synergreen.service.util;

import fr.ifpen.synergreen.config.ApplicationProperties;
import fr.ifpen.synergreen.domain.BatteryManagementRun;
import fr.ifpen.synergreen.service.BatteryManagementRunService;
import fr.ifpen.synergreen.service.FluxTopologyService;
import org.elasticsearch.common.inject.Inject;
import org.glassfish.jersey.internal.guava.Predicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static fr.ifpen.synergreen.service.util.StringFormatUtils.getCurrentTime;
import static java.lang.Thread.currentThread;

@Service
@Transactional
public class WinRunScriptService {

    private Logger log = LoggerFactory.getLogger(WinRunScriptService.class);
    private ApplicationProperties applicationProperties;
    private BatteryManagementRunService batteryManagementRunService;
    private FluxTopologyService fluxTopologyService;

    @Inject
    public WinRunScriptService(BatteryManagementRunService batteryManagementRunService,
                               ApplicationProperties applicationProperties,
                               FluxTopologyService fluxTopologyService) {
        this.batteryManagementRunService = batteryManagementRunService;
        this.applicationProperties = applicationProperties;
        this.fluxTopologyService = fluxTopologyService;
    }

    public ApplicationProperties getApplicationProperties() {
        return applicationProperties;
    }

    public BatteryManagementRunService getBatteryManagementRunService() {
        return batteryManagementRunService;
    }


    /**
     * Execute a line command
     *
     * @return output response and eventual error
     */
    public List<String> execCommand(String cmd) {
        log.debug("Cmd to execute : {}", cmd);

        Process p;
        String s;
        String scriptOutput = null;
        String scriptError = null;

        try {
            p = Runtime.getRuntime().exec(cmd);

            try {
                p.waitFor();
            } catch (InterruptedException f) {
                log.debug("Error: Method didn't wait until process was finished. Abort.");
                f.printStackTrace();
                return Arrays.asList(null, null);
            }
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream(), "Cp852"));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream(), "Cp852"));

            // Standard output of the command
            log.info("Reading the output from the command");
            scriptOutput = "";
            while ((s = stdInput.readLine()) != null) {
                scriptOutput = scriptOutput.concat(s);
            }

            // Standard error of the command (if any)
            log.info("Reading eventual errors from the attempted command");
            scriptError = "";
            while ((s = stdError.readLine()) != null) {
                scriptError = scriptError.concat(s);
            }
            stdInput.close();
            stdError.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Arrays.asList(scriptOutput, scriptError);
    }


    /**
     * Execute a line command
     */
    public void execCommand(String[] cmd, BatteryManagementRun newRun,
                            String inFilename, String outFilename) {

        // Run script and update information of newRun accordingly
        log.info("Processing in separate thread");
        try {
            log.debug("Cmd to execute : {}", (Object[]) cmd);
            try {
                Process process = new ProcessBuilder().inheritIO().command(cmd).start();
                process.waitFor(30, TimeUnit.MINUTES);

                // Kill process
                process.destroy();
                log.info("Process exit code: " + process.exitValue());

                // Read output if existing and update newRun accordingly
                newRun = batteryManagementRunService.saveFinalRunState(newRun, inFilename, outFilename);

                // Close thread
                currentThread().interrupt();

            } catch (InterruptedException e) {
                log.info("InterruptedException during Battery Management cmd execution: " + e.getMessage() + " " + e.getCause());
                newRun = batteryManagementRunService.updateRun(newRun, "terminated", "Incident during process execution", getCurrentTime().truncatedTo(ChronoUnit.SECONDS));
            }

        } catch (IOException e) {
            log.info("IOException during Battery Management cmd execution: " + e.getMessage() + " " + e.getCause());
            newRun = batteryManagementRunService.updateRun(newRun, "terminated", "Incident during process execution", getCurrentTime().truncatedTo(ChronoUnit.SECONDS));
        }
    }


    /**
     * Run Battery Management Script
     *
     * @return output response (output JSON filename) and eventual error
     */
    public BatteryManagementRun execBatteryManagement(BatteryManagementRun newRun,
                                                      String inFilename,
                                                      String outFilename) {

        log.debug("Executing Battery Management Script...");

        final String interpreter = applicationProperties.getBatteryManagementProperties().getInterpreter();
        final String pathToSrc = applicationProperties.getBatteryManagementProperties().getPathToSrc();
        final String pathToResources = applicationProperties.getBatteryManagementProperties().getPathToResources();
        final String pathToScript = applicationProperties.getBatteryManagementProperties().getPackageFolder();
        final String scriptName = applicationProperties.getBatteryManagementProperties().getMasterScript();

//        String cmd = interpreter + " " + pathToSrc+pathToScript+scriptName + " " + inFilename;
        String[] cmd = {interpreter, pathToSrc + pathToScript + scriptName, inFilename};

        // Get previous list of jobs
        List<Long> previousListOfRunningJobs = getRunningProcesses(interpreter + ".exe");

        // Run Battery Management in a separate thread
        final BatteryManagementRun localThreadRun = newRun;
        ZonedDateTime computationStart = getCurrentTime().truncatedTo(ChronoUnit.SECONDS);;
        new Thread(() -> execCommand(cmd, localThreadRun, inFilename, outFilename)).start();

        // Get current list of jobs
        List<Long> nextListOfRunningJobs = getRunningProcesses(interpreter + ".exe");
        Long startTime = System.currentTimeMillis(); //fetch starting time
        while (nextListOfRunningJobs.equals(previousListOfRunningJobs) || (System.currentTimeMillis() - startTime) < 1000) {
            // Thread run has not been started yet
            nextListOfRunningJobs = getRunningProcesses(interpreter + ".exe");
        }

        // Extract the process id of the running job
        Long processId = getNewRunProcessId(previousListOfRunningJobs, nextListOfRunningJobs);
        if (processId == null) {
            newRun = batteryManagementRunService.updateRun(newRun, "terminated", "Incident during process execution", getCurrentTime().truncatedTo(ChronoUnit.SECONDS));
        } else {
            newRun = batteryManagementRunService.updateRun(newRun, "running", computationStart, processId);
        }

        return newRun;
    }


    /**
     * Get list of running processes
     *
     * @return output response (list of PIDs) and eventual error
     */
    public List<Long> getRunningProcesses(String interpreter) {
        // Where interpreter should normally be "python.exe"

        log.debug("Getting list of running processes...");
        List<String> pid = new ArrayList<>();
        List<Long> lpid = new ArrayList<>();
        String cmd = "tasklist /fo csv /fi \"imagename eq " + interpreter + "\"";
        List<String> response = execCommand(cmd);
        if (response.get(1).toLowerCase().contains("information".toLowerCase())) {
            log.error("Error: " + response.get(0));
            pid.add("Error: " + response.get(0));
        } else {
            List<String> tasklistSplitted = Arrays.asList(response.get(0).replace("\"\"", "\",\"").split(","));
            pid = ExtractProcessIds(tasklistSplitted, interpreter);
            for (String myPid : pid) {
                myPid = myPid.replace("\"\"", "\"").replace("\"", "");
                lpid.add(Long.parseLong(myPid));
            }
        }
        return lpid;
    }


    /**
     * Make a readable list of running process PIDs
     *
     * @return output response cleaned (PIDs only)
     */
    public List<String> ExtractProcessIds(List<String> tasklistSplitted, String interpreter) {

        log.debug("Extracting PIDs from taklist");

        List<String> listOfprocessIds = new ArrayList<>();
        for (int i = 0; i < tasklistSplitted.size(); i++) {
            String element = tasklistSplitted.get(i);
            if (element.equals("\"" + interpreter + "\"")) {
                listOfprocessIds.add(tasklistSplitted.get(i + 1));
            }
        }
        return listOfprocessIds;
    }


    /**
     * Kill process based on PID
     *
     * @return output response (confirmation that it's done) and eventual error
     */
    public List<String> killRunningProcess(Long processId) {
        String cmd = "taskkill /f /pid " + processId;
        log.debug("Deleting running process of PID {}...", processId);

        return execCommand(cmd);
    }


    /**
     * Find new run process ID
     *
     * @param processIdsBefore of running python tasks on the machine previous to run exec
     * @param processIdsAfter  list of running python tasks on the machine after run exec
     */
    public Long getNewRunProcessId(List<Long> processIdsBefore, List<Long> processIdsAfter) {

        log.debug("Getting new PID {}...");

        if (!processIdsAfter.isEmpty()) {
            // In case some processes got finished between now and then :
            if (!processIdsBefore.isEmpty()) {
                for (Long oldPid : processIdsBefore) {
                    if (!processIdsAfter.contains(oldPid)) {
                        processIdsBefore.remove(oldPid);
                    }
                }
            }

            List<Long> remainingProcessId = processIdsAfter
                .stream()
                .filter(Predicates.in(new HashSet<>(processIdsBefore)).negate())
                .collect(Collectors.toList());

            if (remainingProcessId.size() > 1) {
                log.debug("There were more than one Process Id created before and after run exec. " +
                    "Taking the first one by default, even if this is possibly a mistake.");
                log.info("Process Id: " + remainingProcessId.get(0));
                return remainingProcessId.get(0);
            } else if (remainingProcessId.size() == 0) {
                log.debug("No process found. Returning Process Id null.");
                return null;
            } else {
                log.info("Process Id: " + remainingProcessId.get(0));
                return remainingProcessId.get(0);
            }
        } else {
            log.debug("No process found. Returning Process Id null.");
            return null;
        }
    }
}
