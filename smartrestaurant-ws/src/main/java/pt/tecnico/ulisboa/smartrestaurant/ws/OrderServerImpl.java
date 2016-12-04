package pt.tecnico.ulisboa.smartrestaurant.ws;

import pt.tecnico.ulisboa.smartrestaurant.domain.DomainFacade;
import pt.tecnico.ulisboa.smartrestaurant.exception.InsecureServerExceptionException;

import javax.jws.WebService;
import java.security.NoSuchAlgorithmException;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.ws.OrderServer")
public class OrderServerImpl implements OrderServer {


    @Override
    public String ping(String msg) {
        System.out.println("Customer " +msg);
        return "Order Pong!";
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

    @Override
    public byte[] login1(String username, byte[] passwordSha2Hash, int tableNo, int OTP) {
        try {
            return DomainFacade.getInstance().login(username, passwordSha2Hash, tableNo, OTP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new InsecureServerExceptionException();
        }
    }

    @Override
    public String requestMyOrdersProducts(byte[] sessionId) {
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
