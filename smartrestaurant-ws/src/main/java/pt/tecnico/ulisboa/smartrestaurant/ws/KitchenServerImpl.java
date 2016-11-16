package pt.tecnico.ulisboa.smartrestaurant.ws;

import pt.tecnico.ulisboa.smartrestaurant.domain.DomainFacade;
import pt.tecnico.ulisboa.smartrestaurant.handler.SmartRestarantHandler;

import javax.jws.HandlerChain;
import javax.jws.WebService;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.ws.KitchenServer")
@HandlerChain(file= "/kitchen_handler-chain.xml")
public class KitchenServerImpl implements KitchenServer {

    public KitchenServerImpl(){
        SmartRestarantHandler.handlerConstants.SENDER_SERVICE_NAME = "OrderServer";
        SmartRestarantHandler.handlerConstants.RCPT_SERVICE_NAME = "KitchenServer";
    }

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
