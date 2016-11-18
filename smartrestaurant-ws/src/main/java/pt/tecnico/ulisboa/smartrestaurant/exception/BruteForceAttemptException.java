package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class BruteForceAttemptException extends SmartRestaurantException {

    @Override
    public String getMessage() {
        return "Brute Force attempt detected, cooldown activated";
    }
}
