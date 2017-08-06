package by.gstu.kharaneka.exceptions;

/**
 * Created by nadia on 03.08.2017.
 */
public class FloorsNumberException extends Exception {
    private String message;

    public FloorsNumberException(int floors){
        if(floors<0){
            message="The number of floors can't be negative.";
        }
        if(floors<2){
            message="The number of floors can't be less than 2.";
        }
    }

    @Override
    public String getMessage() {
        return message;
    }
}
