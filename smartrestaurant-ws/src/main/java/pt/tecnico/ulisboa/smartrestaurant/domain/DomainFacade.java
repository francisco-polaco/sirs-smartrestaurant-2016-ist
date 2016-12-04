package pt.tecnico.ulisboa.smartrestaurant.domain;

import pt.ist.fenixframework.Atomic;

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
    public void registerNewUser(String username, byte[] hashedPassword, String firstName, String lastName, int nif){
        SmartRestaurantManager.getInstance().registerNewUser(username, hashedPassword, firstName, lastName, nif);
    }

    @Atomic
    public byte[] login(String username, byte[] hashedPassword, int tableNo) throws NoSuchAlgorithmException {
        return SmartRestaurantManager.getInstance().login(username, hashedPassword, tableNo);
    }

    @Atomic
    public byte[] login(String username, byte[] hashedPassword, int tableNo, int OTP) throws NoSuchAlgorithmException {
        return SmartRestaurantManager.getInstance().login(username, hashedPassword, tableNo, OTP);
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
    public void orderProducts(byte[] sessionId, byte[] hashedPassword){
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
    public void confirmPayment(byte[] sessionId, byte[] hashedPassword, String paypalReference){
        SmartRestaurantManager.getInstance().confirmPayment(sessionId, hashedPassword, paypalReference);
    }

    @Atomic
    public double getPaymentDetails(byte[] sessionId){
        return SmartRestaurantManager.getInstance().getPaymentDetails(sessionId);
    }

}
