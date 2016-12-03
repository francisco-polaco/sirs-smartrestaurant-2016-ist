package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class OrderDoesntExistException extends SmartRestaurantException {

    private String message;

    public OrderDoesntExistException(String s) {
        message = s;
    }

    public OrderDoesntExistException() {
    }

    @Override
    public String getMessage() {
        if(message == null)
            return "An order with that id doesn't exists.";
        else return message;
    }
}
