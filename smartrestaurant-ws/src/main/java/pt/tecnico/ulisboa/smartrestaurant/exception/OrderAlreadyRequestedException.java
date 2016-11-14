package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class OrderAlreadyRequestedException extends SmartRestaurantException {

    @Override
    public String getMessage() {
        return "That order was already requested.";
    }
}
