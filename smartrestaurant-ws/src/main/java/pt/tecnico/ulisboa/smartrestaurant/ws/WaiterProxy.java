package pt.tecnico.ulisboa.smartrestaurant.ws;

import pt.tecnico.ulisboa.smartrestaurant.handler.SmartRestaurantOutboundHandler;
import pt.tecnico.ulisboa.smartrestaurant.waiter.ws.WaiterRealServer;
import pt.tecnico.ulisboa.smartrestaurant.waiter.ws.WaiterRealServerImplService;

import javax.xml.ws.BindingProvider;
import java.util.Map;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

/**
 * Created by francisco on 03/12/2016.
 */
public class WaiterProxy {

    private WaiterRealServer _port;

    public static String endpointAddress = null;

    public WaiterProxy() {
        System.out.println("Creating stub ...");
        WaiterRealServerImplService _service = new WaiterRealServerImplService();
        _port = _service.getWaiterRealServerImplPort();

        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) _port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        if(endpointAddress == null) requestContext.put(ENDPOINT_ADDRESS_PROPERTY, "http://192.168.1.10:5050/waiter-ws/endpoint");
        else requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
    }

    public String ping(String msg){
        synchronized (this){
            SmartRestaurantOutboundHandler.handlerConstants.RCPT_SERVICE_NAME = "Waiter";}
        return _port.ping(msg);
    }

    public void requestToDeliverOrder(long orderId){
        synchronized (this){
            SmartRestaurantOutboundHandler.handlerConstants.RCPT_SERVICE_NAME = "Waiter";}
        _port.requestToDeliverOrder(orderId);
    }


}
