package pt.tecnico.ulisboa.smartrestaurant.kitchenServer.ws.cli;

import pt.tecnico.ulisboa.smartrestaurant.handler.SmartRestarantHandler;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Created by tiago on 11/17/16.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.kitchenServer.ws.cli.KitchenClientServer")
@HandlerChain(file= "/kitchen_handler-chain.xml")
public class KitchenClientServerImpl implements KitchenClientServer{
    private ArrayList<Long> _list = new ArrayList<Long>();

    @Override
    public void addList(long id){
        SmartRestarantHandler.handlerConstants.SENDER_SERVICE_NAME = "KitchenServer";
        SmartRestarantHandler.handlerConstants.RCPT_SERVICE_NAME = "OrderServer";
        _list.add(id);
        System.out.println("Novo pedido id "+id);
    }

    @Override
    public String getList() {
        SmartRestarantHandler.handlerConstants.SENDER_SERVICE_NAME = "KitchenServer";
        SmartRestarantHandler.handlerConstants.RCPT_SERVICE_NAME = "KitchenClient";
        String result="";
        for(Long l:_list){
            result+= l.toString() + "\n";
        }
        System.out.println("lista\n" + result);
        return result;
    }

    @Override
    public void removeList(long id){
        SmartRestarantHandler.handlerConstants.SENDER_SERVICE_NAME = "KitchenServer";
        SmartRestarantHandler.handlerConstants.RCPT_SERVICE_NAME = "KitchenClient";
        _list.remove(id);
    }
}
