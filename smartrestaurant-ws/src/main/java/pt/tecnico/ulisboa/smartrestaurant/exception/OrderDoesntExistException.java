package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class OrderDoesntExistException extends SmartRestaurantException {

    @Override
    public String getMessage() {
        return "An order with that id doesn't exists.";
    }
}
