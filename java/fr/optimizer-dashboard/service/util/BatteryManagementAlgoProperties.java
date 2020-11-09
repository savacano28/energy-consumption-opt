package fr.ifpen.synergreen.service.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class BatteryManagementAlgoProperties {

    public static void mainTest(String[] args) {

        try (OutputStream output = new FileOutputStream("src/main/resources/.batteryManagementAlgo.properties")) {

            Properties prop = new Properties();

            // set the properties value
            prop.setProperty("T", "1400.");
            prop.setProperty("DT", "10.");
            prop.setProperty("E0tfFactor", "1.");   // Etf = {E0tfFactor}*E0. Default is Etf = E0.
//            prop.setProperty("interpreter", "python");
//            prop.setProperty("pathToSrc", "src/main/python/fr/ifpen/fr/synergreen/");
//            prop.setProperty("pathToResources", "src/main/resources/");
//            prop.setProperty("package", "battery_management/");
//            prop.setProperty("script", "BatteryManagementScript.py");

            // save properties to project root folder
            prop.store(output, null);

            System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }

    }
}
