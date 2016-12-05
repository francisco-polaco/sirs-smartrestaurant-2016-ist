package pt.tecnico.ulisboa.smartrestaurant.ws;

import pt.tecnico.ulisboa.smartrestaurant.domain.DomainFacade;

import javax.jws.HandlerChain;
import javax.jws.WebService;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.ws.WaiterSoftServer")
@HandlerChain(file= "/kitchen_handler-chain.xml")
public class WaiterSoftServerImpl implements WaiterSoftServer {
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
