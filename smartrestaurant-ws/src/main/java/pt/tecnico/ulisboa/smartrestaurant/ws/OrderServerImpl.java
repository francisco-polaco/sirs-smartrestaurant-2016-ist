package pt.tecnico.ulisboa.smartrestaurant.ws;

import pt.tecnico.ulisboa.smartrestaurant.domain.DomainFacade;
import pt.tecnico.ulisboa.smartrestaurant.exception.InsecureServerExceptionException;

import javax.jws.WebService;
import java.io.UnsupportedEncodingException;
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
    public void registerNewUser(String username, String password, String firstName, String lastName, int nif) {
        try {
            DomainFacade.getInstance().registerNewUser(username, password, firstName, lastName, nif);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new InsecureServerExceptionException();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new InsecureServerExceptionException();
        }
    }

    @Override
    public void logOut(byte[] sessionId){
            DomainFacade.getInstance().logOut(sessionId);
    }

    @Override
    public byte[] login(String username, String password, int tableNo, int OTP) {
        try {
            return DomainFacade.getInstance().login(username, password, tableNo, OTP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new InsecureServerExceptionException();
        }catch (UnsupportedEncodingException e) {
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
    public void orderProducts(byte[] sessionId, String passwordSha2Hash) {
        try {
            DomainFacade.getInstance().orderProducts(sessionId, passwordSha2Hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new InsecureServerExceptionException();
        }
    }

    @Override
    public void confirmPayment(byte[] sessionId, String passwordSha2Hash, String paypalReference) {
        try {
            DomainFacade.getInstance().confirmPayment(sessionId, passwordSha2Hash, paypalReference);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new InsecureServerExceptionException();
        }
    }

    @Override
    public double getPaymentDetails(byte[] sessionId) {
        return DomainFacade.getInstance().getPaymentDetails(sessionId);
    }

}
