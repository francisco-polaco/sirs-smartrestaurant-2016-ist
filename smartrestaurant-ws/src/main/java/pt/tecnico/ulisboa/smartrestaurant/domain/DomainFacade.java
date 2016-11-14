package pt.tecnico.ulisboa.smartrestaurant.domain;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.ulisboa.smartrestaurant.ws.*;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
    public byte[] login(String username, byte[] hashedPassword,int tableNo) throws NoSuchAlgorithmException {
        return SmartRestaurantManager.getInstance().login(username, hashedPassword, tableNo);
    }

    @Atomic
    public void addProductToOrder(byte[] sessionId, String productName){
        SmartRestaurantManager.getInstance().addRequestToOrder(sessionId, productName);
    }

    @Atomic
    public List<ProductProxy> requestMyOrdersProducts(byte[] sessionId){
        List<Product> productList = SmartRestaurantManager.getInstance().requestMyOrdersProducts(sessionId);
        List<ProductProxy> productProxies = new ArrayList<>();
        for(Product p : productList){
            productProxies.add(new ProductProxy(p));
        }
        return productProxies;
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
    public void setOrderToPayed(long orderToPayed){
        SmartRestaurantManager.getInstance().setOrderToPayed(orderToPayed);
    }
}
