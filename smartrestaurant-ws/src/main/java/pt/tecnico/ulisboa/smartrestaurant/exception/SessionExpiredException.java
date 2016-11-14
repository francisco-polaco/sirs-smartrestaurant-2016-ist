package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class SessionExpiredException extends SmartRestaurantException {

    private String name;

    public SessionExpiredException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return name + " expired.";
    }
}
