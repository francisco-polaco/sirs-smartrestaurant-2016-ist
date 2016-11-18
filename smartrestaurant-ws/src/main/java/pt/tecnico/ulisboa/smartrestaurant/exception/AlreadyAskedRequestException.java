package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by tiago on 11/18/16.
 */
public class AlreadyAskedRequestException extends SmartRestaurantException {

    @Override
    public String getMessage() {
        return "Request already made!";
    }
}
