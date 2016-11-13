package pt.tecnico.ulisboa.smartrestaurant.domain;

import javax.jws.WebService;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.domain.Server")
public class ServerImpl implements Server {
    @Override
    public String ping(String msg) {
        System.out.println(msg);
        return "Pong!";
    }
}
