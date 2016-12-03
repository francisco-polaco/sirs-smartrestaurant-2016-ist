package pt.tecnico.ulisboa.smartrestaurant.ws;

import pt.tecnico.ulisboa.smartrestaurant.domain.DomainFacade;

import javax.jws.WebService;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.ws.WaiterServer")
public class WaiterServerImpl implements WaiterServer {
    @Override
    public String ping(String msg) {
        System.out.println("Waiter " + msg);
        return "Order Pong!";
    }

    @Override
    public void setOrderToDelivered(long orderId) {
        DomainFacade.getInstance().setOrderToDelivered(orderId);
    }
}
