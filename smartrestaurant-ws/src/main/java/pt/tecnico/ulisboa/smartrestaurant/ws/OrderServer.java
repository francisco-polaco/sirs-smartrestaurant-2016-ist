package pt.tecnico.ulisboa.smartrestaurant.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService
public interface OrderServer {
    @WebMethod String ping(String msg);

    @WebMethod void registerNewUser(String username, String password, String firstName, String lastName, int nif);

    @WebMethod byte[] login(String username, String passwordSha2Hash, int tableNo, int OTP);

    @WebMethod String requestMyOrdersProducts(byte[] sessionId);

    @WebMethod void addProductToOrder(byte[] sessionId, String productName);

    @WebMethod void orderProducts(byte[] sessionId, String passwordSha2Hash);

    @WebMethod void confirmPayment(byte[] sessionId, String passwordSha2Hash, String paypalReference);

    @WebMethod double getPaymentDetails(byte[] sessionId);

    @WebMethod void logOut(byte [] sessionId);

}
