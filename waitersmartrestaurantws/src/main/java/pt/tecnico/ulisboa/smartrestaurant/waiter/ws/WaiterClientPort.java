package pt.tecnico.ulisboa.smartrestaurant.waiter.ws;

import pt.tecnico.ulisboa.smartrestaurant.ws.WaiterServer;
import pt.tecnico.ulisboa.smartrestaurant.ws.WaiterServerImplService;

import javax.xml.ws.BindingProvider;
import java.util.Map;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

/**
 * Created by francisco on 03/12/2016.
 */
public class WaiterClientPort {
    private WaiterServer port;

    public WaiterClientPort(String endpointAddress) {
        System.out.println("Creating stub ...");
        WaiterServerImplService service = new WaiterServerImplService();
        port = service.getWaiterServerImplPort();

        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
    }

    public String ping(){
        return port.ping("Ping");
    }
}
