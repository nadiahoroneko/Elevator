package by.gstu.kharaneka.exceptions;

/**
 * Created by nadia on 03.08.2017.
 */
public class ElevatorCapacityExceptions extends Exception {
    private String message;

    public ElevatorCapacityExceptions(int capacity){
        if(capacity<0){
            message="The capacity of the elevator can't be negative.";
        }
        if(capacity==0){
            message="The capacity of the elevator can't be 0.";
        }
    }

    @Override
    public String getMessage() {
        return message;
    }
}
