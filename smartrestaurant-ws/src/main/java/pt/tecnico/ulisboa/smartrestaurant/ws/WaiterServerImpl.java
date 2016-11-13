package pt.tecnico.ulisboa.smartrestaurant.ws;

import javax.jws.WebService;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.ws.WaiterServer")
public class WaiterServerImpl implements WaiterServer {
    @Override
    public void requestDelivered(long requestId) {

    }
}
