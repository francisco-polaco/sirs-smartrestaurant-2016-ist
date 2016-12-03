package pt.tecnico.ulisboa.smartrestaurant.waiter.ws;

import javax.jws.WebService;
import java.math.BigInteger;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.waiter.ws.WaiterRealServer")
public class WaiterRealServerImpl implements WaiterRealServer {
    private BigInteger orderIdToDeliver;

    public BigInteger getOrderIdToDeliver() {
        if(orderIdToDeliver == null) throw  new IllegalStateException("You didn't have an order assign.");
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
