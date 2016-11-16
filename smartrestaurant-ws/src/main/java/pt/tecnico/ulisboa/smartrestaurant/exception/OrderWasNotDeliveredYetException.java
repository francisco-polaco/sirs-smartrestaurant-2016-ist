package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class OrderWasNotDeliveredYetException extends SmartRestaurantException {

    @Override
    public String getMessage() {
        return "You need to wait for your order to be delivered.";
    }
}
