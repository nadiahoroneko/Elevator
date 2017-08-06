package by.gstu.kharaneka.controllers;

import by.gstu.kharaneka.entities.Passenger;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by nadia on 20.07.2017.
 */
public class Elevator
{
    private static final Logger logger = Logger.getLogger(Elevator.class);

    private Lock lock;
    private int floor; //текущий этаж
    List<Passenger> passengers; //список пассажиров, перемещающихся на лифте
    private int capacity;//вместимость
    int goUp;

    public Elevator(int capacity) {
        passengers = new ArrayList<>();
        this.capacity = capacity;
        goUp=1;
        lock = new ReentrantLock();
    }

    public int getFloor() {
        return floor;
    }

    public int getCapacity() {
        return capacity;
    }

    public void goUp() {
        floor++;
    }

    public void goDown() {
        floor--;
    }
    public int getGoUp(){
        return goUp;
    }
    public void setGoUp(int goUp){
        this.goUp=goUp;
    }
    public boolean isEmpty() {
        if (passengers.size() == 0)
            return true;
        else
            return false;
    }
    public boolean isNotEmpty() {
        if (passengers.size() > 0)
            return true;
        else
            return false;
    }

    public boolean isFull() {
        if (passengers.size() == capacity)
            return true;
        else
            return false;
    }
    public boolean isNotFull() {
        if (passengers.size() < capacity)
            return true;
        else
            return false;
    }

    public void addPassenger(Passenger p) {
        lock.lock();
        try {
            if (isNotFull()) {
                passengers.add(p);
                p.setStatus(2);
                logger.info("The passenger " + p.getPassengerId() + " boarded the elevator on the " + (getFloor()+1) + " floor");
            }
        } finally {
            lock.unlock();
        }
    }

    public List<Passenger> getPassengers(){
        return passengers;
    }

    public void removePass(Passenger p) {
        lock.lock();
        try {
        Iterator<Passenger> iterPas=passengers.iterator();
        while (iterPas.hasNext()){
            Passenger pas=iterPas.next();
            if (isNotEmpty() && pas.getPassengerId() == p.getPassengerId()) {
                p.setStatus(3);
                iterPas.remove();
                pas.setStatus(3);
            }
        }
        } finally {
            lock.unlock();
        }
    }

    public List<Passenger> removePassenger() {
        List<Passenger> listRemove=new ArrayList<>();
        Iterator<Passenger> iterPas=passengers.iterator();
        while (iterPas.hasNext()){
            Passenger pas=iterPas.next();
            if (!isEmpty() && pas.getFloorFinish() == floor && pas.getStatus() == 2) {
                listRemove.add(pas);
                iterPas.remove();

            }
        }
        return listRemove;
    }
}
