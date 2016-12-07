package pt.tecnico.ulisboa.smartrestaurant.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService
public interface WaiterSoftServer {

    @WebMethod String ping(String msg);

    @WebMethod void setOrderToDelivered(long orderId);
}
