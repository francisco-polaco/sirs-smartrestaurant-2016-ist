package pt.tecnico.ulisboa.smartrestaurant.ws;

import pt.tecnico.ulisboa.smartrestaurant.domain.DomainFacade;

import javax.jws.WebService;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.ws.KitchenServer")
public class KitchenServerImpl implements KitchenServer {

    @Override
    public String ping(String msg) {
        System.out.println("Kitchen " + msg);
        return "Pong!";
    }

    @Override
    public void setOrderReadyToDeliver(long orderId) {
        DomainFacade.getInstance().setOrderReadyToDeliver(orderId);
    }
}
