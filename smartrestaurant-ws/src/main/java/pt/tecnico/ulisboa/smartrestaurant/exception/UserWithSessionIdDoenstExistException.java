package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class UserWithSessionIdDoenstExistException extends SmartRestaurantException {

    @Override
    public String getMessage() {
        return "A user with that session id doesn't exists.";
    }
}
