package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class AccessDeniedException extends SmartRestaurantException {

    @Override
    public String getMessage() {
        return "Your access was denied.";
    }
}
