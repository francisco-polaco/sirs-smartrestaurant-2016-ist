package pt.tecnico.ulisboa.smartrestaurant.ws;

import javax.jws.WebService;
import java.util.List;

/**
 * Created by francisco on 13/11/2016.
 */
@WebService
public interface OrderServer {
    String ping(String msg);

    byte[] login(String username, byte[] passwordSha2Hash, int tableNo);

//    List<ProductProxy> requestAllProducts();

    List<ProductProxy> requestMyOrdersProducts(byte[] sessionId);

    void addProductToOrder(byte[] sessionId, String productName);

    void orderProducts(byte[] sessionId, byte[] passwordSha2Hash);

}
