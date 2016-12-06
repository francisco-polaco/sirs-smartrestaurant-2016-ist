package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class AuthenticatorCodeRejectedException extends SmartRestaurantException {

    @Override
    public String getMessage() {
        return "The authentication code provided is not valid.";
    }
}
