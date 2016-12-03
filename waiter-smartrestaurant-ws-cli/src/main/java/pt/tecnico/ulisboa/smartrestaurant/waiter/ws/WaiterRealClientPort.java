package pt.tecnico.ulisboa.smartrestaurant.waiter.ws;

import pt.tecnico.ulisboa.smartrestaurant.ws.WaiterSoftServer;
import pt.tecnico.ulisboa.smartrestaurant.ws.WaiterSoftServerImplService;

import javax.xml.ws.BindingProvider;
import java.util.Map;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

/**
 * Created by francisco on 03/12/2016.
 */
public class WaiterRealClientPort {
    private WaiterSoftServer port;

    public WaiterRealClientPort(String endpointAddress) {
        System.out.println("Creating stub ...");
        WaiterSoftServerImplService service = new WaiterSoftServerImplService();
        port = service.getWaiterSoftServerImplPort();

        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
    }

    public String ping(){
        return port.ping("Ping");
    }

    public void setOrderToDelivered(long orderId){
        port.setOrderToDelivered(orderId);
    }
}
