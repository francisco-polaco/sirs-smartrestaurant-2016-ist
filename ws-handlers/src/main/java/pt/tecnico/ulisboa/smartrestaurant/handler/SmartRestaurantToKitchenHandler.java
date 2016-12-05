package pt.tecnico.ulisboa.smartrestaurant.handler;

/**
 * Created by xxlxpto on 07-05-2016.
 */

public class SmartRestaurantToKitchenHandler extends SmartRestaurantInboundHandler {


    @Override
    public void initialize() {
        handlerConstants = new HandlerConstants();
        handlerConstants.SENDER_SERVICE_NAME = "OrderServer";
        handlerConstants.RCPT_SERVICE_NAME = "KitchenServer";
    }
}