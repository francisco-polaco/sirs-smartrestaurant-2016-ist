package pt.tecnico.ulisboa.smartrestaurant.handler;

/**
 * Created by francisco on 05/12/2016.
 */
public abstract class SmartRestaurantInboundHandler extends SmartRestaurantHandler {
    public HandlerConstants handlerConstants;

    @Override
    public HandlerConstants getHandlerConstants() {
        return handlerConstants;
    }
}
