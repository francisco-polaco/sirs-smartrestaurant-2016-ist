package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class UserLoginDoesntExistException extends SmartRestaurantException {

    @Override
    public String getMessage() {
        return "A user with that login doesn't exists.";
    }
}
