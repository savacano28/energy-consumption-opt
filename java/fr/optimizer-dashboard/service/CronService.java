package fr.ifpen.synergreen.service;

import fr.ifpen.synergreen.domain.FluxPeriod;
import fr.ifpen.synergreen.domain.Invoice;
import fr.ifpen.synergreen.repository.BatteryManagerRepository;
import fr.ifpen.synergreen.repository.BatteryModelSourceRepository;
import fr.ifpen.synergreen.repository.FluxNodeRepository;
import fr.ifpen.synergreen.repository.FluxTopologyRepository;
import fr.ifpen.synergreen.service.dto.StateFluxNodeDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.time.ZonedDateTime;


@Service
public class CronService {

    private final FluxTopologyRepository fluxTopologyRepository;
    private final FluxNodeRepository fluxNodeRepository;
    private final FluxTopologyService fluxTopologyService;
    private final FluxNodeService fluxNodeService;
    private final BatteryModelSourceRepository batteryModelSourceRepository;
    private final InvoiceService invoiceService;
    private final BatteryManagerRepository batteryManagerRepository;

    public CronService(FluxTopologyRepository fluxTopologyRepository,
                       FluxTopologyService fluxTopologyService,
                       FluxNodeRepository fluxNodeRepository,
                       FluxNodeService fluxNodeService,
                       BatteryModelSourceRepository batteryModelSourceRepository,
                       InvoiceService invoiceService,
                       BatteryManagerRepository batteryManagerRepository) {
        this.fluxTopologyRepository = fluxTopologyRepository;
        this.fluxTopologyService = fluxTopologyService;
        this.fluxNodeRepository = fluxNodeRepository;
        this.fluxNodeService = fluxNodeService;
        this.batteryModelSourceRepository = batteryModelSourceRepository;
        this.invoiceService = invoiceService;
        this.batteryManagerRepository = batteryManagerRepository;
    }

    /*fixme change periode de cron selon besoin*/
    @Scheduled(cron = "0 0/1 * * * *")
    @Transactional
    public void computeInvoice() {
        FluxPeriod fp = new FluxPeriod();
        fp.setStart(ZonedDateTime.now().minusMinutes(30));
        fp.setEnd(ZonedDateTime.now());
        fp.setStep(600L); //10 minutes
        ZonedDateTime instant = ZonedDateTime.now();
        Month lastMonth = instant.minusMonths(1).getMonth();
        Integer lastYear = instant.minusYears(1).getYear();

        fluxTopologyService.findAllReal().stream().forEach(f -> {
            Double invoice, accInvoice;
            StateFluxNodeDTO sNode = f.getStateSummary(fp).get(0);
            Invoice currentInv = f.getInvoices().stream().filter(i -> i.getMonth().equals(instant.getMonth()) && i.getYear().equals(instant.getYear())).findAny().orElse(null);

            if (currentInv == null) {
                currentInv = new Invoice();
                currentInv.setFluxTopology(f);
                currentInv.setMonth(instant.getMonth());
                currentInv.setYear(instant.getYear());
                invoice = 0.;
                if (!instant.getMonth().equals(Month.JANUARY)) {
                    accInvoice = f.getInvoices().stream().filter(i -> i.getMonth().equals(lastMonth) && i.getYear().equals(instant.getYear())).findAny().get().getAccAmount();
                } else {
                    accInvoice = f.getInvoices().stream().filter(i -> i.getMonth().equals(Month.DECEMBER) && i.getYear().equals(lastYear)).findAny().get().getAccAmount();
                }
            } else {
                invoice = currentInv.getAmount();
                accInvoice = currentInv.getAccAmount();
            }

            currentInv.setAmount(invoice + (sNode.getPriceCons().get("HP") + sNode.getPriceCons().get("HC")));
            currentInv.setAccAmount(accInvoice + (sNode.getPriceCons().get("HP") + sNode.getPriceCons().get("HC")));
            currentInv.setLastUpdateInvoice(instant.toEpochSecond());
            f.getInvoices().add(currentInv);
            //invoiceService.save(currentInv);
            fluxTopologyRepository.save(f);
        });
    }

    /*Test getValues */
   /* @Scheduled(cron = "0 0/1 * * * *")
    @Transactional
    public void updateValuesInElementMeasure() {
        FluxNode fluxNode = fluxNodeService.findOne(18L);
        BatteryModelSource batteryModelSource = (BatteryModelSource) fluxNode.getMeasurementSource();

        *//*List Parameters*//*
        fluxNode.getParameters().add(new Parameter("testParm", "testParmLb", ParameterType.STRING, "TEST in Nodes", null));
        batteryModelSource.getParameters().stream().forEach(p -> {
            if (p.getParameterName().equals("historicPower")) {
                p.setParameterValue(p.getParameterValue().replace("}", "," + ZonedDateTime.now() + "=" + Math.random() * 10) + "}");
            }
        });

      /*  Instruction i = new Instruction();
        i.setCommand(10.8);
        i.setId(10L);
        i.setStart(ZonedDateTime.now());
        i.setEnd(ZonedDateTime.now().plusMinutes(4));
        batteryModelSource.setInstruction(i);

        batteryModelSourceRepository.save(batteryModelSource);
        fluxNodeRepository.save(fluxNode);
    }*/

  /*  @Scheduled(cron = "0 0/1 * * * *")
    @Transactional
    public void batteryManagetTest() {
        FluxTopology fluxTopology = fluxTopologyService.findOne(34L);

        BatteryManager batteryManager = new BatteryManager();
        batteryManager.setFluxTopology(fluxTopology);
      //  batteryManager.setListOfJobs(Collections.emptyList());

        fluxTopology.setBatteryManager(batteryManager);

        batteryManagerRepository.save(batteryManager);
        //fluxTopologyRepository.save(fluxTopology);


    }*/

}
