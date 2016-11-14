package pt.tecnico.ulisboa.smartrestaurant.ws;

import javax.jws.WebService;
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
    public byte[] login(String username, byte[] passwordSha2Hash) {

        return passwordSha2Hash;
    }

    @Override
    public List<String> requestAllProducts() {
        return null;
    }

    @Override
    public List<String> requestMyOrdersProducts(byte[] sessionId) {
        return null;
    }

    @Override
    public void addProductToOrder(byte[] sessionId, String productName) {

    }

    @Override
    public void orderProducts(byte[] sessionId) {

    }

}
