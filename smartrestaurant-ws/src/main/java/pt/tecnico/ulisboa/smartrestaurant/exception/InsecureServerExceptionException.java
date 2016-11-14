package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class InsecureServerExceptionException extends SmartRestaurantException {

    @Override
    public String getMessage() {
        return "The server you are tying to reach is insecure.";
    }
}
