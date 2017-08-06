package by.gstu.kharaneka.entities;

import by.gstu.kharaneka.controllers.Elevator;
import org.apache.log4j.Logger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import static java.lang.Thread.sleep;

/**
 * Created by nadia on 20.07.2017.
 */
public class Passenger{
    private static final Logger logger = Logger.getLogger(Passenger.class);

    private int passengerId;
    private int status; //0-незапущен; 1 - ожидание; 2 - едет; 3 - закончил.
    private int floorRun;
    private int floorFinish;
    private int goUp;

    public Passenger(int passengerId,int floorRun, int floorFinish,int goUp) {
        this.passengerId=passengerId;
        this.status = 0;
        this.floorRun = floorRun;
        this.floorFinish = floorFinish;
        this.goUp=goUp;
    }

    public int getFloorRun() {
        return floorRun;
    }

    public int getFloorFinish() {
        return floorFinish;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public int getGoUp() {
        return goUp;
    }

    public void setGoUp(int goUp) {
        this.goUp = goUp;
    }

    public class TranportationTask implements Runnable {
        Object sync;
        Elevator elevator;
        public TranportationTask(Elevator elevator, Object sync){
            this.elevator=elevator;
            this.sync=sync;
        }

        @Override
        public void run() {

            String name = Thread.currentThread().getName();
            logger.info("The passenger " + getPassengerId() +
                    " moves from " + (getFloorRun()+1) +
                    " to " + (getFloorFinish()+1) + " floor");
            setStatus(1);
            while (getStatus() != 3) {
                synchronized (sync) {
                    //logger.info("Пассажир в ожидании " + passsenger.getPassengerId());
                    try {
                        sync.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //logger.info("Пассажир запущен ===================" + passsenger.getPassengerId());
                if (getStatus() == 1 &&
                        getFloorRun() == elevator.getFloor() &&
                        getGoUp()==elevator.getGoUp()) {
                    //logger.debug("add "+passsenger.getPassengerId());
                    elevator.addPassenger(Passenger.this);
                }
                if (getStatus() == 2 &&
                        getFloorFinish() == elevator.getFloor()) {
                    //logger.debug(j"remove "+passsenger.getPassengerId());
                    elevator.removePass(Passenger.this);
                }

            }

            logger.info("The passenger " + getPassengerId() + " left the elevator on the " + (getFloorFinish()+1)+"th floor");
            //condition.signalAll();

        }
    }

}
