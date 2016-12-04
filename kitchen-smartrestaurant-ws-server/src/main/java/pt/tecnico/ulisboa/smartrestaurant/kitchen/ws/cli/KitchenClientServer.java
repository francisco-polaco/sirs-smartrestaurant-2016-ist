package pt.tecnico.ulisboa.smartrestaurant.kitchen.ws.cli;

import javax.jws.WebService;

/**
 * Created by tiago on 11/17/16.
 */
@WebService
public interface KitchenClientServer {
    void addList(long id);

}
