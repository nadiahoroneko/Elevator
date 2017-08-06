package by.gstu.kharaneka.exceptions;

/**
 * Created by nadia on 03.08.2017.
 */
public class PassagersNumberExceptions extends Exception {

    String message;

    public PassagersNumberExceptions(int passangersNumber){
        if(passangersNumber==0){
            message="The number of passengers can't be 0.";
        }
        if(passangersNumber<0){
            message="The number of passengers can't be negative.";
        }
    }

    @Override
    public String getMessage() {
        return message;
    }
}

