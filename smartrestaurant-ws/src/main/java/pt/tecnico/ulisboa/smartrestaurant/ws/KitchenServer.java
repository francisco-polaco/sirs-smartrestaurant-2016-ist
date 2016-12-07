package pt.tecnico.ulisboa.smartrestaurant.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService
public interface KitchenServer {
    @WebMethod String ping(String msg);

    @WebMethod void setOrderReadyToDeliver(long orderId);
}
