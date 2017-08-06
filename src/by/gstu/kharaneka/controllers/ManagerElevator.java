package by.gstu.kharaneka.controllers;

import by.gstu.kharaneka.containers.PassengerList;
import by.gstu.kharaneka.entities.Passenger;
import by.gstu.kharaneka.graph.ElevatorGraph;
import by.gstu.kharaneka.graph.Slider;
import by.gstu.kharaneka.graph.TextAreaAppender;
import by.gstu.kharaneka.reader.ReadConfig;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by nadia on 20.07.2017.
 */
public class ManagerElevator {

    private static final Logger logger = Logger.getLogger(ManagerElevator.class);


    private ExecutorService executorService;
    private Elevator elevator;
    private int passengerNumber;
    private List<Object> syncList;
    private PassengerList passengerList;
    private int floorNumber;

    private boolean sideMove = true;
    ElevatorGraph elevatorGraph;
    JFrame w;
    JFrame wLog;
    int heightElevator=40;
    JFrame jFrameSlider;
    Slider slider=new Slider();

    Object sync;

    public ManagerElevator(ReadConfig readConfig) {
        this.passengerNumber = readConfig.getPassengersNumber();
        this.floorNumber = readConfig.getFloors();
        executorService = Executors.newFixedThreadPool(passengerNumber);
        elevator = new Elevator(readConfig.getElevatorCapacity());
    }

    public ManagerElevator(int floor, int passengerNumber, int capacity) {
        this.passengerNumber = passengerNumber;
        this.floorNumber = floor;
        executorService = Executors.newFixedThreadPool(passengerNumber);
        elevator = new Elevator(capacity);
    }

    public void initialization() {
        initListPassager();
        syncList = new ArrayList<Object>();

        for (int i = 0; i < passengerNumber; i++) {
            sync = new Object();
            syncList.add(sync);
            Thread thread = new Thread(passengerList.getPassenger(i).new TranportationTask( elevator, syncList.get(i)), Integer.toString(i));
            executorService.execute(thread);
            //new Thread(passengerList.getPassenger(i).new TranportationTask(), Integer.toString(i)).start();
        }
        executorService.shutdown();
        wLogger();
    }

    public void initListPassager() {

        passengerList = new PassengerList();
        Random rand = new Random();
        int floorRun;
        int floorFinish;
        int goUp = 0;
        for (int i = 0; i < passengerNumber; i++) {
            do {
                floorRun = rand.nextInt(floorNumber);
                floorFinish = rand.nextInt(floorNumber);
            } while (floorRun == floorFinish);
            if (floorFinish > floorRun) goUp = 1;
            else goUp = 0;
            passengerList.addPassenger(i + 1, floorRun, floorFinish, goUp);
        }
    }

    public void run() {
        passengerList.allStart();
        grafElevator();
        elevatorGraph.paintElevator(elevator.getFloor(), passengerList.getPassengerList(),slider.getValue());
        while (passengerNumber() > 0) {
            if (elevator.isNotEmpty()) {
                kickPassager();
                elevatorGraph.paintElevator(elevator.getFloor(), passengerList.getPassengerList(),slider.getValue());
            }
            getPassager();
            elevatorGraph.paintElevator(elevator.getFloor(), passengerList.getPassengerList(),slider.getValue());
            if (isMove()) {
                move();
                elevatorGraph.paintElevator(elevator.getFloor(), passengerList.getPassengerList(),slider.getValue());
            }
        }
        grafElevatorClose();
        wLogClose();
        sliderClose();
    }

    public int passengerNumber() {
        int kp = 0;
        for (Passenger p : passengerList.getPassengerList()) {
            if (p.getStatus() < 3) kp++;
        }
        return kp;
    }

    //посадка пассажиров
    public void getPassager() {

        for (int i = 0; i < passengerList.getSize(); i++) {
            if (elevator.getFloor() == passengerList.getPassenger(i).getFloorRun() && passengerList.getPassenger(i).getStatus() == 1 &&
                    passengerList.getPassenger(i).getGoUp() == elevator.getGoUp()) {
                //logger.debug("---" + i);
                synchronized (syncList.get(i)) {
                    syncList.get(i).notify();
                }
            }
        }
    }

    //выкидывание пассажиров из лифта
    public void kickPassager() {
            for (int i = 0; i < passengerList.getSize(); i++) {
                if ( passengerList.getPassengerList().get(i).getStatus()==2) {
                    synchronized (syncList.get(i)) {
                        syncList.get(i).notify();
                    }
                }
            }
    }

    public boolean isMove() {

        boolean ok = true;
        while (ok) {
            //logger.debug("---");
            if (elevator.isFull()) ok = false;
            int kp = 0;
            for (Passenger p : passengerList.getPassengerList()) {
                if (p.getFloorRun() == elevator.getFloor() && p.getStatus() == 1 && p.getGoUp() == elevator.getGoUp())
                    kp++;
            }
            if (kp == 0) ok = false;
        }
        return true;
    }

    //move elevator
    public void move() {

        if (elevator.getFloor() == floorNumber - 1) {
            sideMove = false;
            elevator.setGoUp(0);
        }
        if (elevator.getFloor() == 0) {
            sideMove = true;
            elevator.setGoUp(1);
        }
        int tmpfloor = elevator.getFloor();
        if (sideMove) {
            elevator.goUp();
        } else {
            elevator.goDown();
        }
        if (elevator.getFloor() == 0) elevator.setGoUp(1);
        if (elevator.getFloor() == floorNumber - 1) elevator.setGoUp(0);
        logger.info("Elevator moved from " + (tmpfloor + 1) + " to " + (elevator.getFloor() + 1) + " floor");
    }

    public void grafElevator() {

            /* Задание заголовка окна*/
        w = new JFrame("Elevator");
    /*Задание размеров окна*/
    int wWidth=heightElevator*(passengerList.getSize()/2+elevator.getCapacity());
    int wHeight=100 + heightElevator * (floorNumber + 1);
        w.setSize(wWidth, wHeight);

/* 	Если у окна не будет функции закрытия,
 *	при нажатии крестика окно не закроется.*/
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JSlider js1 = new JSlider();

/*	Менеджер определяет
 *  каким образом в окне расположены объекты.*/

        w.setLayout(new BorderLayout(1, 1));
        elevatorGraph = new ElevatorGraph(floorNumber, elevator.getCapacity(), passengerList.getPassengerList(),heightElevator,wWidth);
        w.add(elevatorGraph);
        w.setVisible(true);
        slider();
    }
    public void slider(){

        jFrameSlider=new JFrame("Elevator speed");
        jFrameSlider.setSize(300,100);
        //Slider slider=new Slider();
        jFrameSlider.add(slider);
        int wHeight=100 + heightElevator * (floorNumber + 1);
        jFrameSlider.setLocation(100,wHeight);
        jFrameSlider.setVisible(true);

    }

    public void grafElevatorClose() {
        w.setVisible(false);
        w.dispose();

    }

    public void wLogger() {
        //Создадим окно с JTextPane и покажем его
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane sp = new JScrollPane(textArea);
        wLog = new JFrame("Logger");
        wLog.setSize(500, 500);
        wLog.getContentPane().add(sp);
        //wLog.add(textArea);
        int wWidth=heightElevator*(passengerList.getSize()/2+elevator.getCapacity());

        wLog.setLocation(wWidth,0);
        wLog.setVisible(true);
        wLog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Сконфигурируем логгер, чтобы он использовал наш appender
        Logger rootLogger = LogManager.getRootLogger();
        rootLogger.addAppender(new TextAreaAppender(textArea));

    }

    public void wLogClose() {
        wLog.setVisible(false);
        wLog.dispose();

    }
    public void sliderClose() {
        jFrameSlider.setVisible(false);
        jFrameSlider.dispose();

    }
}

