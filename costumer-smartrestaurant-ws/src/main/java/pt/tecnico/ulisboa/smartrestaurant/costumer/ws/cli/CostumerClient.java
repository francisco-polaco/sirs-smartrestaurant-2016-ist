package pt.tecnico.ulisboa.smartrestaurant.costumer.ws.cli;

import javax.xml.ws.BindingProvider;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pt.tecnico.ulisboa.smartrestaurant.ws.OrderServer;
import pt.tecnico.ulisboa.smartrestaurant.ws.OrderServerImplService;
import pt.tecnico.ulisboa.smartrestaurant.ws.ProductProxy;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

/**
 * Created by tiago on 11/15/16.
 */
public class CostumerClient {
    private OrderServer _port;
    private OrderServerImplService _service;
    private byte[] _sessionId = null;

    public CostumerClient(String endpointAddress){
        if (endpointAddress == null) {
            System.out.println("Not found!");
            return;
        } else {
            System.out.printf("Found %s%n", endpointAddress);
        }

        System.out.println("Creating stub ...");
        _service = new OrderServerImplService();
        _port = _service.getOrderServerImplPort();

        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) _port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
        //requestContext.put(JAXWSProperties.SSL_SOCKET_FACTORY, batata().getServerSocketFactory());
    }

    public String ping(String pingMessage){
        return _port.ping(pingMessage);
    }

    public String login(String username, String password, int tableNo){
        try {
            _sessionId = _port.login(username, pw2sha2(password), tableNo);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Falha no login!";
        }
        return "Sucesso login!";
    }

    public int readQRCode(){
        Random generator = new Random();
        return generator.nextInt(10);
    }

    public String requestMyOrderProducts(){
        if(!checkLogin()){
            return "Need to login!";
        }
        System.out.println(listProducts(_port.requestMyOrdersProducts(_sessionId)));

        return "";
    }

    public String addProductToOrder(String meal){
        if(!checkLogin()){
            return "Need to login!";
        }
        try {
            _port.addProductToOrder(_sessionId, meal);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Erro no pedido";
        }
        return "Pedido efetuado com sucesso!";
    }

    private byte[] pw2sha2(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(password.getBytes(StandardCharsets.UTF_8));
    }

    private boolean checkLogin(){
        if(_sessionId == null)
            return false;
        return true;
    }

    private String listProducts(List<ProductProxy> products){
        String result = "";
        for(ProductProxy p: products){
            result += p.toString();
        }
        return result;
    }
}
