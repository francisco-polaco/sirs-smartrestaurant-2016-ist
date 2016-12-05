package pt.tecnico.ulisboa.smartrestaurant.kitchen.ws.cli;

import pt.tecnico.ulisboa.smartrestaurant.handler.SmartRestaurantOutboundHandler;
import pt.tecnico.ulisboa.smartrestaurant.ws.KitchenServer;
import pt.tecnico.ulisboa.smartrestaurant.ws.KitchenServerImplService;

import javax.xml.ws.BindingProvider;
import java.util.Map;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

/**
 * Created by tiago on 11/15/16.
 */
public class KitchenClientImpl{
    private KitchenServer _port;

    public KitchenClientImpl(String endpointAddress){
        if (endpointAddress == null) {
            System.out.println("Not found!");
            return;
        } else {
            System.out.printf("Found %s%n", endpointAddress);
        }

        System.out.println("Creating stub ...");

        KitchenServerImplService _service = new KitchenServerImplService();
        _port = _service.getKitchenServerImplPort();

        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) _port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

    }

    public String ping(String pingMessage){
        setStrings();
        return _port.ping(pingMessage);
    }

    private synchronized void setStrings() {
        SmartRestaurantOutboundHandler.handlerConstants.SENDER_SERVICE_NAME = "KitchenServer";
        SmartRestaurantOutboundHandler.handlerConstants.RCPT_SERVICE_NAME = "OrderServer";
    }

    public String setOrderReadyToDeliver(long orderId){
//        if(!_list.contains(new Long(orderId))){
//            return "Wrong id number";
//        }
        setStrings();
        try{
            _port.setOrderReadyToDeliver(orderId);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Item wasn't set";
        }
        //removeList(orderId);
        return "Item set to ready!";
    }

/*    public String listAllOrders(){
        return _port2.getList();
    }

    private String removeList(long id){
        SmartRestaurantHandler.handlerConstants.SENDER_SERVICE_NAME = "KitchenClient";
        SmartRestaurantHandler.handlerConstants.RCPT_SERVICE_NAME = "KitchenServer";
        try{
            _port2.removeList(id);
        }catch (Exception e ){
            System.err.println(e.getMessage());
            return "Could not removed\n";
        }
        return "Removed\n";
    }*/

}
