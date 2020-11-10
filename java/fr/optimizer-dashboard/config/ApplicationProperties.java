package fr.ifpen.synergreen.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

/**
 * Properties specific to Synergreen.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String repertoireUpload;
    private BatteryManagementProperties batteryManagementProperties;

    public String getRepertoireUpload() {
        return repertoireUpload == null ? null : (Paths.get(repertoireUpload).toString() + "/");
    }

    public void setRepertoireUpload(String repertoireUpload) {
        this.repertoireUpload = repertoireUpload;
    }

    public BatteryManagementProperties getBatteryManagementProperties() {
        return batteryManagementProperties;
    }

    public void setBatteryManagementProperties(BatteryManagementProperties batteryManagementProperties) {
        this.batteryManagementProperties = batteryManagementProperties;
    }


    public static class BatteryManagementProperties {

        private String interpreter;
        private String pathToSrc;
        private String pathToResources;
        private String packageFolder;
        private String masterScript;

        public String getInterpreter() {
            return interpreter == null ? null : (Paths.get(interpreter).toString());
        }

        public void setInterpreter(String interpreter) {
            this.interpreter = interpreter;
        }

        public String getPathToSrc() {
            return pathToSrc == null ? null : (Paths.get(pathToSrc).toString() + "/");
        }

        public void setPathToSrc(String pathToSrc) {
            this.pathToSrc = pathToSrc;
        }

        public String getPathToResources() {
                return pathToResources == null ? null : (Paths.get(pathToResources).toString() + "/");
        }

        public void setPathToResources(String pathToResources) {
            this.pathToResources = pathToResources;
        }

        public String getPackageFolder() {
                return packageFolder == null ? null : (Paths.get(packageFolder).toString() + "/");
        }

        public void setPackageFolder(String packageFolder) {
            this.packageFolder = packageFolder;
        }

        public String getMasterScript() {
            return masterScript == null ? null : (Paths.get(masterScript).toString());
        }

        public void setMasterScript(String masterScript) {
            this.masterScript = masterScript;
        }
    }
}
