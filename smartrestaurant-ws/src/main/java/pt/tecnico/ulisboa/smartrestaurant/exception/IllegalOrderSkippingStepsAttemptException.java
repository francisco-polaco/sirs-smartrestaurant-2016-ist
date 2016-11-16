package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class IllegalOrderSkippingStepsAttemptException extends SmartRestaurantException {

    @Override
    public String getMessage() {
        return "You are trying to skip the natural step order for an order. No can do mate!";
    }
}
