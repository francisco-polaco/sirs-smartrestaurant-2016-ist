package pt.tecnico.ulisboa.smartrestaurant.kitchenServer.ws.cli;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by tiago on 11/17/16.
 */
@WebService
public interface KitchenClientServer {
    void addList(long id);

    String getList();

    void removeList(long id);

}
