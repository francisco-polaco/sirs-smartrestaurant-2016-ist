package pt.tecnico.ulisboa.smartrestaurant.customer.ws.cli;

import javax.xml.ws.BindingProvider;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

import org.joda.time.DateTime;
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
    private DateTime _loginTime;
    private static final int TIMEOUT_SESSION_TIME = 1800000;

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

    public String login(String username, String password, int tableNo, int otp){
        try {
            _sessionId = _port.login(username, password, tableNo, otp);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Falha no login!";
        }
        _loginTime = new DateTime();
        return "Sucesso login!";
    }

    public int readQRCode(){
        Random generator = new Random();
        return generator.nextInt(10);
    }

    public String requestMyOrderProducts(){
        String list = _port.requestMyOrdersProducts(_sessionId);
        if(list.equals("")){
            list = "A lista está vazia\n";
        }
        _loginTime = new DateTime();
        return list;
    }

    public String addProductToOrder(String meal){
        try {
            _port.addProductToOrder(_sessionId, meal);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Erro no pedido";
        }
        _loginTime = new DateTime();
        return "Pedido efetuado com sucesso!";
    }

    public String registerNewUser(String username, String pw, String firstName, String lastName, int nif){
        try {
            _port.registerNewUser(username, pw, firstName, lastName, nif);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Falha no registo!";
        }
        return "Registo efetuado com sucesso!";
    }

    public String orderProducts(String password){
        try {
            _port.orderProducts(_sessionId, password);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Falha no pedido!";
        }
        _loginTime = new DateTime();
        return "Pedido efetuado com sucesso!";
    }

    public String confirmPayment(String password, String paypalReference){
        try {
            _port.confirmPayment(_sessionId, password, paypalReference);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Falha na confirmação de pagamento!";
        }
        _loginTime = new DateTime();
        return "Confirmação de pagamento efetuada com sucesso!";
    }

    public String getPaymentDetails(){
        String value;
        try {
             value = String.valueOf(_port.getPaymentDetails(_sessionId));
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Falha na confirmação de pagamento!";
        }
        _loginTime = new DateTime();
        return value;
    }


//    private byte[] pw2sha2(String password) throws NoSuchAlgorithmException {
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        return digest.digest(password.getBytes(StandardCharsets.UTF_8));
//    }

    public boolean checkLogin(){
        if(_sessionId ==null)
            return false;

        return true;
    }

    public String logOut(){
        try {
            _port.logOut(_sessionId);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return "Falha no logout!";
        }
        _sessionId = null;
        return "Logout Efetuado com Sucesso";
    }

    public void checkSessionExpired(){
        if (_loginTime != null) {
            if (new DateTime().getMillis() - _loginTime.getMillis() > TIMEOUT_SESSION_TIME) {
                logOut();
            }
        }
    }
}
