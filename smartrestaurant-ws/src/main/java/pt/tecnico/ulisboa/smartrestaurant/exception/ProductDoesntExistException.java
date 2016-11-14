package pt.tecnico.ulisboa.smartrestaurant.exception;

/**
 * Created by francisco on 13/11/2016.
 */
public class ProductDoesntExistException extends SmartRestaurantException {

    private String name;

    public ProductDoesntExistException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return name + " doesn't exists.";
    }
}
