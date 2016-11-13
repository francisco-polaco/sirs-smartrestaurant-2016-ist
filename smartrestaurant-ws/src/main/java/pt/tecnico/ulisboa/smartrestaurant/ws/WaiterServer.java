package pt.tecnico.ulisboa.smartrestaurant.ws;

import javax.jws.WebService;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService
public interface WaiterServer {
    void requestDelivered(long requestId);
}
