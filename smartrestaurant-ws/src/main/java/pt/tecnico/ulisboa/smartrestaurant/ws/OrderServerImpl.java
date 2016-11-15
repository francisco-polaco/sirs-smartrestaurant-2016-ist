package pt.tecnico.ulisboa.smartrestaurant.ws;

import pt.tecnico.ulisboa.smartrestaurant.domain.DomainFacade;
import pt.tecnico.ulisboa.smartrestaurant.exception.InsecureServerExceptionException;

import javax.jws.WebService;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.ws.OrderServer")
public class OrderServerImpl implements OrderServer {
    @Override
    public String ping(String msg) {
        System.out.println("Customer " +msg);
        return "Pong!";
    }

    @Override
    public void registerNewUser(String username, byte[] hashedPassword, String firstName, String lastName, int nif) {
        DomainFacade.getInstance().registerNewUser(username, hashedPassword, firstName, lastName, nif);
    }

    @Override
    public byte[] login(String username, byte[] passwordSha2Hash, int tableNo) {

        try {
            return DomainFacade.getInstance().login(username, passwordSha2Hash, tableNo);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new InsecureServerExceptionException();
        }
    }

    /*@Override
    public List<ProductProxy> requestAllProducts() {
        return null;
    }*/

    @Override
    public List<ProductProxy> requestMyOrdersProducts(byte[] sessionId) {
        return DomainFacade.getInstance().requestMyOrdersProducts(sessionId);
    }

    @Override
    public void addProductToOrder(byte[] sessionId, String productName) {
        DomainFacade.getInstance().addProductToOrder(sessionId, productName);
    }

    @Override
    public void orderProducts(byte[] sessionId, byte[] passwordSha2Hash) {
        DomainFacade.getInstance().orderProducts(sessionId, passwordSha2Hash);
    }

    @Override
    public void confirmPayment(byte[] sessionId, byte[] passwordSha2Hash, String paypalReference) {
        DomainFacade.getInstance().confirmPayment(sessionId, passwordSha2Hash, paypalReference);
    }

    @Override
    public double getPaymentDetails(byte[] sessionId) {
        return DomainFacade.getInstance().getPaymentDetails(sessionId);
    }

}
