package pt.tecnico.ulisboa.smartrestaurant.ws;

import javax.jws.WebService;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService
public interface OrderServer {
    String ping(String msg);

    void registerNewUser(String username, byte[] hashedPassword, String firstName, String lastName, int nif);

    byte[] login(String username, byte[] passwordSha2Hash, int tableNo);

    byte[] login1(String username, byte[] passwordSha2Hash, int tableNo, int OTP);

    String requestMyOrdersProducts(byte[] sessionId);

    void addProductToOrder(byte[] sessionId, String productName);

    void orderProducts(byte[] sessionId, byte[] passwordSha2Hash);

    void confirmPayment(byte[] sessionId, byte[] passwordSha2Hash, String paypalReference);

    double getPaymentDetails(byte[] sessionId);

}
