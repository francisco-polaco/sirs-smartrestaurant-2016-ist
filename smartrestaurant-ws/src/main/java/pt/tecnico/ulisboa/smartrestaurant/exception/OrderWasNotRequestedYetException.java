package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class OrderWasNotRequestedYetException extends SmartRestaurantException {

    @Override
    public String getMessage() {
        return "You need to finish your order first.";
    }
}
