package by.gstu.kharaneka;

import by.gstu.kharaneka.controllers.ManagerElevator;
import by.gstu.kharaneka.reader.ReadConfig;

import java.io.IOException;

public class Main {
    int passengersNumber;
    int elevatorCapacity;
    public static void main(String[] args) throws IOException {
        ReadConfig readConfig=new ReadConfig();
        ManagerElevator manager = new ManagerElevator(readConfig);
        manager.initialization();
        manager.run();
    }

    }
