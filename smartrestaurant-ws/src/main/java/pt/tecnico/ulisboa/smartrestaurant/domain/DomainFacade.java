package pt.tecnico.ulisboa.smartrestaurant.domain;

import pt.ist.fenixframework.Atomic;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by francisco on 13/11/2016.
 */
public class DomainFacade {

    private static DomainFacade instance;

    public static DomainFacade getInstance(){
        if(instance == null) instance = new DomainFacade();
        return instance;
    }

    @Atomic
    public void registerNewUser(String username, String password, String firstName, String lastName, int nif) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        SmartRestaurantManager.getInstance().registerNewUser(username, password, firstName, lastName, nif);
    }

    @Atomic
    public byte[] login(String username, String password, int tableNo, int OTP) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return SmartRestaurantManager.getInstance().login(username, password, tableNo, OTP);
    }

    @Atomic
    public void addProductToOrder(byte[] sessionId, String productName){
        SmartRestaurantManager.getInstance().addRequestToOrder(sessionId, productName);
    }

    @Atomic
    public void logOut(byte[] sessionId){
        SmartRestaurantManager.getInstance().logOut(sessionId);
    }

    @Atomic
    public String requestMyOrdersProducts(byte[] sessionId){
        List<Product> productList = SmartRestaurantManager.getInstance().requestMyOrdersProducts(sessionId);
        String res = "";
        for(Product p : productList){
            res += p.toString() + "\n";
        }
        return res;
    }

    @Atomic
    public void orderProducts(byte[] sessionId, String hashedPassword) throws NoSuchAlgorithmException {
        SmartRestaurantManager.getInstance().orderProducts(sessionId, hashedPassword);
    }

    @Atomic
    public void setOrderReadyToDeliver(long orderId){
        SmartRestaurantManager.getInstance().setOrderReadyToDeliver(orderId);
    }

    @Atomic
    public void setOrderToDelivered(long orderId){
        SmartRestaurantManager.getInstance().setOrderToDelivered(orderId);
    }

    @Atomic
    public void confirmPayment(byte[] sessionId, String hashedPassword, String paypalReference) throws NoSuchAlgorithmException {
        SmartRestaurantManager.getInstance().confirmPayment(sessionId, hashedPassword, paypalReference);
    }

    @Atomic
    public double getPaymentDetails(byte[] sessionId){
        return SmartRestaurantManager.getInstance().getPaymentDetails(sessionId);
    }

}
