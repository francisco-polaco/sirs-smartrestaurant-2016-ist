package pt.tecnico.ulisboa.smartrestaurant.kitchen.ws.cli;

import javax.xml.ws.BindingProvider;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

import pt.tecnico.ulisboa.smartrestaurant.handler.SmartRestarantHandler;
import pt.tecnico.ulisboa.smartrestaurant.ws.KitchenServer;
import pt.tecnico.ulisboa.smartrestaurant.ws.KitchenServerImplService;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

/**
 * Created by tiago on 11/15/16.
 */
public class KitchenClient {
    private KitchenServer _port;
    private KitchenServerImplService _service;
    private byte[] _sessionId = null;

    public KitchenClient(String endpointAddress){
        if (endpointAddress == null) {
            System.out.println("Not found!");
            return;
        } else {
            System.out.printf("Found %s%n", endpointAddress);
        }

        System.out.println("Creating stub ...");
        SmartRestarantHandler.handlerConstants.SENDER_SERVICE_NAME = "KitchenServer";
        SmartRestarantHandler.handlerConstants.RCPT_SERVICE_NAME = "OrderServer";

        _service = new KitchenServerImplService();
        _port = _service.getKitchenServerImplPort();

        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) _port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
        //requestContext.put(JAXWSProperties.SSL_SOCKET_FACTORY, batata().getServerSocketFactory());
    }

    public String ping(String pingMessage){
        return _port.ping(pingMessage);
    }

    public String setOrderReadyToDeliver(long orderId){
        try{
            _port.setOrderReadyToDeliver(orderId);
        }catch (Exception e){ System.err.println(e.getMessage()); }
        return "Item set to ready!";
    }
}
