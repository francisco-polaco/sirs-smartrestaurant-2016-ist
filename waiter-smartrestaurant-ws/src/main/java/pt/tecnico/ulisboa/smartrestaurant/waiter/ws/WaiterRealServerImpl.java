package pt.tecnico.ulisboa.smartrestaurant.waiter.ws;

import pt.tecnico.ulisboa.smartrestaurant.handler.SmartRestaurantOutboundHandler;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.math.BigInteger;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.waiter.ws.WaiterRealServer")
@HandlerChain(file= "/kitchen_handler-chain.xml")
public class WaiterRealServerImpl implements WaiterRealServer {
    private BigInteger orderIdToDeliver;

    public WaiterRealServerImpl() {
        SmartRestaurantOutboundHandler.handlerConstants.SENDER_SERVICE_NAME = "Waiter";
        SmartRestaurantOutboundHandler.handlerConstants.RCPT_SERVICE_NAME = "OrderServer";
    }

    public BigInteger getOrderIdToDeliver() {
        if(orderIdToDeliver == null) throw  new IllegalStateException("You don't have an order assign.");
        return orderIdToDeliver;
    }

    public void setOrderIdToDeliver(BigInteger orderIdToDeliver) {
        this.orderIdToDeliver = orderIdToDeliver;
    }

    @Override
    public String ping(String msg) {
        System.out.println("Order " + msg);
        return "Waiter Pong!";
    }

    @Override
    public void requestToDeliverOrder(long orderId) {
        orderIdToDeliver = BigInteger.valueOf(orderId);
    }

}
