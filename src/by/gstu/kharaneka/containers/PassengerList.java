package by.gstu.kharaneka.containers;

import by.gstu.kharaneka.entities.Passenger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nadia on 03.08.2017.
 */
public class PassengerList {
    public static final List<Passenger> passengerList= Collections.synchronizedList(new ArrayList<Passenger>());

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public void addPassenger(int passengerId, int floorRun, int floorFinish, int goUp){
        Passenger passenger=new Passenger(passengerId,floorRun,floorFinish,goUp);
        this.passengerList.add(passenger);
    }

    public Passenger getPassenger(int i){
        return passengerList.get(i);
    }

    public int getSize(){
        return passengerList.size();
    }
    //ожидание, пока все потоки пассажиров не начнут ждать

    public boolean allStart() {
        boolean allStart = false;
        while (!allStart) {
            allStart = true;
            for (Passenger p : passengerList) {
                if (p.getStatus() == 0) allStart = false;
            }
        }
        return allStart;
    }
}
