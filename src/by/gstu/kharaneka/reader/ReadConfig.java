package by.gstu.kharaneka.reader;

import by.gstu.kharaneka.exceptions.ElevatorCapacityExceptions;
import by.gstu.kharaneka.exceptions.FloorsNumberException;
import by.gstu.kharaneka.exceptions.PassagersNumberExceptions;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Properties;

public class ReadConfig {
    private static final Logger logger = Logger.getLogger(ReadConfig.class);

    private int floors;
    private int passengersNumber;
    private int elevatorCapacity;

    public ReadConfig() throws IOException {
        getPropValues();
    }

    public void getPropValues() throws IOException {
        String result = "";
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            String path = new File("").getAbsolutePath();
            //logger.debug(path);
            String propFileName = "config.properties";
            String propFileNameNew = path+"\\"+propFileName;
            //logger.debug(propFileName);
            File f=new File(propFileNameNew);
            if(!f.exists() || f.isDirectory()) {
                copyFileUsingStream(f);
            }
            inputStream = new FileInputStream(f);
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            // get the property value and print it out

            floors= Integer.parseInt(prop.getProperty("floors"));
            elevatorCapacity= Integer.parseInt(prop.getProperty("elevatorCapacity"));
            passengersNumber= Integer.parseInt(prop.getProperty("passengersNumber"));
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }

    public int getFloors(){
        if(floors<2){
            try {
                throw new FloorsNumberException(floors);
            } catch (FloorsNumberException e) {
                //e.printStackTrace();
                logger.error(e.getMessage());
                floors=2;
            }
        }
        return floors;
    }

    public int getPassengersNumber(){
        if(passengersNumber<1) {
            try {
                throw new PassagersNumberExceptions(passengersNumber);
            } catch (PassagersNumberExceptions e) {
                // passagersNumberExceptions.printStackTrace();
                logger.error(e.getMessage());
                passengersNumber=1;
            }
        }
        return passengersNumber;
    }

    public int getElevatorCapacity(){
        if(elevatorCapacity<1){
            try {
                throw new ElevatorCapacityExceptions(elevatorCapacity);
            } catch (ElevatorCapacityExceptions e) {
                //elevatorCapacityExceptions.printStackTrace();
                logger.error(e.getMessage());
                elevatorCapacity=1;
            }
        }
        return elevatorCapacity;
    }
    private void copyFileUsingStream(File dest) throws IOException {
    InputStream is = null;
    OutputStream os = null;
        String propFileName = "config.properties";
    try {
        is = getClass().getClassLoader().getResourceAsStream(propFileName);
        os = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    } finally {
        is.close();
        os.close();
    }
    }
}
