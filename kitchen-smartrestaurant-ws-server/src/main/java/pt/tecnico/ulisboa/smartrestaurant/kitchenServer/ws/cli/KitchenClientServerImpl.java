package pt.tecnico.ulisboa.smartrestaurant.kitchenServer.ws.cli;

import javax.jws.WebService;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Created by tiago on 11/17/16.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.kitchenServer.ws.cli.KitchenClientServer")
public class KitchenClientServerImpl implements KitchenClientServer, Serializable{
    private ArrayList<Long> _list = new ArrayList<Long>();

    @Override
    public void addList(long id){
        _list.add(new Long(id));
        System.out.println("Novo pedido id "+id);
    }

    @Override
    public String getList() {
        String result="";
        for(Long l:_list){
            result+= l.toString() + "\n";
        }
        return result;
    }
}
