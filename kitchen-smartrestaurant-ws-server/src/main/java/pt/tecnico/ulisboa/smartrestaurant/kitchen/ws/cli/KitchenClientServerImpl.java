package pt.tecnico.ulisboa.smartrestaurant.kitchen.ws.cli;

import pt.tecnico.ulisboa.smartrestaurant.handler.SmartRestarantHandler;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.util.ArrayList;

/**
 * Created by tiago on 11/17/16.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.kitchen.ws.cli.KitchenClientServer")
@HandlerChain(file= "/kitchen_handler-chain.xml")
public class KitchenClientServerImpl implements KitchenClientServer{
    private ArrayList<Long> _list = new ArrayList<Long>();

    public ArrayList<Long> getList() {
        return _list;
    }

    public void removeList(Long toRemove){
        _list.remove(toRemove);
    }

    @Override
    public void addList(long id){
        _list.add(id);
        System.out.println("Novo pedido id "+id);
    }


}
