package pt.tecnico.ulisboa.smartrestaurant.waiter.ws;

import javax.jws.WebService;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService
public interface WaiterRealServer {

    String ping(String msg);

    void requestToDeliverOrder(long orderId);
}
