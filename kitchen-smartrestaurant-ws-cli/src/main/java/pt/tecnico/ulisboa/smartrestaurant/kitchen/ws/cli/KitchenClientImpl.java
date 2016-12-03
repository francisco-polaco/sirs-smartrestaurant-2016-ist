package pt.tecnico.ulisboa.smartrestaurant.kitchen.ws.cli;

import pt.tecnico.ulisboa.smartrestaurant.handler.SmartRestarantHandler;
import pt.tecnico.ulisboa.smartrestaurant.ws.KitchenServer;
import pt.tecnico.ulisboa.smartrestaurant.ws.KitchenServerImplService;
import pt.tecnico.ulisboa.smartrestaurant.kitchenserver.ws.cli.KitchenClientServer;
import pt.tecnico.ulisboa.smartrestaurant.kitchenserver.ws.cli.KitchenClientServerImplService;

import javax.jws.WebService;
import javax.xml.ws.BindingProvider;
import java.util.Map;


import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

/**
 * Created by tiago on 11/15/16.
 */
public class KitchenClientImpl{
    private KitchenServer _port;
    private KitchenServerImplService _service;
    private KitchenClientServer _port2;
    private KitchenClientServerImplService _service2;

    public KitchenClientImpl(String endpointAddress, String endpointAddress2){
        if (endpointAddress == null || endpointAddress2 == null) {
            System.out.println("Not found!");
            return;
        } else {
            System.out.printf("Found %s%n", endpointAddress);
        }

        System.out.println("Creating stub ...");

        _service = new KitchenServerImplService();
        _port = _service.getKitchenServerImplPort();

        _service2 = new KitchenClientServerImplService();
        _port2 = _service2.getKitchenClientServerImplPort();


        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) _port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider2 = (BindingProvider) _port2;
        Map<String, Object> requestContext2 = bindingProvider2.getRequestContext();
        requestContext2.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress2);
    }

    public String ping(String pingMessage){
        SmartRestarantHandler.handlerConstants.SENDER_SERVICE_NAME = "KitchenClient";
        SmartRestarantHandler.handlerConstants.RCPT_SERVICE_NAME = "OrderServer";
        return _port.ping(pingMessage);
    }

    public String setOrderReadyToDeliver(long orderId){
        SmartRestarantHandler.handlerConstants.SENDER_SERVICE_NAME = "KitchenClient";
        SmartRestarantHandler.handlerConstants.RCPT_SERVICE_NAME = "OrderServer";
//        if(!_list.contains(new Long(orderId))){
//            return "Wrong id number";
//        }
        try{
            _port.setOrderReadyToDeliver(orderId);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Item wasn't set";
        }
        removeList(orderId);
        return "Item set to ready!";
    }

    public String listAllOrders(){
        SmartRestarantHandler.handlerConstants.SENDER_SERVICE_NAME = "KitchenClient";
        SmartRestarantHandler.handlerConstants.RCPT_SERVICE_NAME = "KitchenServer";
        return _port2.getList();
    }

    public String removeList(long id){
        SmartRestarantHandler.handlerConstants.SENDER_SERVICE_NAME = "KitchenClient";
        SmartRestarantHandler.handlerConstants.RCPT_SERVICE_NAME = "KitchenServer";
        try{
            _port2.removeList(id);
        }catch (Exception e ){
            System.err.println(e.getMessage());
            return "Could not removed\n";
        }
        return "Removed\n";
    }

}
