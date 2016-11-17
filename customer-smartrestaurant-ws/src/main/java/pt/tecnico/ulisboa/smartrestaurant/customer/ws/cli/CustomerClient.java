package pt.tecnico.ulisboa.smartrestaurant.customer.ws.cli;

import javax.xml.ws.BindingProvider;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

import pt.tecnico.ulisboa.smartrestaurant.ws.OrderServer;
import pt.tecnico.ulisboa.smartrestaurant.ws.OrderServerImplService;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

/**
 * Created by tiago on 11/15/16.
 */
public class CustomerClient {
    private OrderServer _port;
    private OrderServerImplService _service;
    private byte[] _sessionId = null;

    public CustomerClient(String endpointAddress){
        if (endpointAddress ==null) {
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
        System.out.println(_port.requestMyOrdersProducts(_sessionId));

        return "";
    }

    public String addProductToOrder(String meal){
        try {
            _port.addProductToOrder(_sessionId, meal);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Erro no pedido";
        }
        return "Pedido efetuado com sucesso!";
    }

    public String registerNewUser(String username, String pw, String firstName, String lastName, int nif){
        try {
            _port.registerNewUser(username, pw2sha2(pw), firstName, lastName, nif);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Falha no registo!";
        }
        return "Registo efetuado com sucesso!";
    }

    public String orderProducts(String password){
        try {
            _port.orderProducts(_sessionId, pw2sha2(password));
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Falha no pedido!";
        }
        return "Pedido efetuado com sucesso!";
    }

    public String confirmPayment(String password, String paypalReference){
        try {
            _port.confirmPayment(_sessionId, pw2sha2(password), paypalReference);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Falha na confirmação de pagamento!";
        }
        return "Confirmação de pagamento efetuada com sucesso!";
    }

    public String getPaymentDetails(){
        try {
            return String.valueOf(_port.getPaymentDetails(_sessionId));
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Falha na confirmação de pagamento!";
        }
    }


    private byte[] pw2sha2(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(password.getBytes(StandardCharsets.UTF_8));
    }

    public boolean checkLogin(){
        if(_sessionId ==null)
            return false;
        return true;
    }
}