package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class OrderIsNotRequestedYetException extends SmartRestaurantException {

    @Override
    public String getMessage() {
        return "You need to finish your order first.";
    }
}
