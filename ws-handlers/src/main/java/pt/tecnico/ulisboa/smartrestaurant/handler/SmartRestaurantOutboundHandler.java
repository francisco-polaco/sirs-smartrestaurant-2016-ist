package pt.tecnico.ulisboa.smartrestaurant.handler;

/**
 * Created by francisco on 05/12/2016.
 */
public class SmartRestaurantOutboundHandler extends SmartRestaurantHandler {
    public static HandlerConstants handlerConstants = new HandlerConstants();

    @Override
    public void initialize() {

    }

    @Override
    public HandlerConstants getHandlerConstants() {
        return handlerConstants;
    }
}
