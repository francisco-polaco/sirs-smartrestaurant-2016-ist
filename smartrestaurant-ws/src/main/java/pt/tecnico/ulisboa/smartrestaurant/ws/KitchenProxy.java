package pt.tecnico.ulisboa.smartrestaurant.ws;


import pt.tecnico.ulisboa.smartrestaurant.handler.SmartRestarantHandler;
import pt.tecnico.ulisboa.smartrestaurant.kitchen.ws.cli.KitchenClientServer;
import pt.tecnico.ulisboa.smartrestaurant.kitchen.ws.cli.KitchenClientServerImplService;

import javax.xml.ws.BindingProvider;
import java.util.Map;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

/**
 * Created by tiago on 12/3/16.
 */
public class KitchenProxy {

    private KitchenClientServer _port;

    public KitchenProxy(String endpointAddress) {

        System.out.println("Creating stub ...");
        KitchenClientServerImplService _service = new KitchenClientServerImplService();
        _port = _service.getKitchenClientServerImplPort();

        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) _port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

    }

    public void addList(long id){
        synchronized (this){SmartRestarantHandler.handlerConstants.RCPT_SERVICE_NAME = "KitchenServer";}
        try {
            _port.addList(id);
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}
