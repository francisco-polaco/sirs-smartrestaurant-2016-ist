package pt.tecnico.ulisboa.smartrestaurant.domain;

import javax.jws.WebService;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService
public interface Server {
    String ping(String msg);
}
