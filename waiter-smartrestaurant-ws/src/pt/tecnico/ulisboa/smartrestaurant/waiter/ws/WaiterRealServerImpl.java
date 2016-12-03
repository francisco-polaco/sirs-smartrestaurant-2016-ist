package pt.tecnico.ulisboa.smartrestaurant.waiter.ws;

import javax.jws.WebService;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.waiter.ws.WaiterRealServer")
public class WaiterRealServerImpl implements WaiterRealServer {
    public WaiterRealServerImpl() {
    }

    @Override
    public String ping(String msg) {
        System.out.println("Order " + msg);
        return "Waiter Pong!";
    }

    @Override
    public void requestToDeliverOrder(long orderId) {

    }

}
