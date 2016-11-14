package pt.tecnico.ulisboa.smartrestaurant.domain;

public class Product extends Product_Base {
    
    public Product() {
        super();
    }

    Product(String productName, String productDescription, double price, SmartRestaurantManager smartRestaurantManager){
        setName(productName);
        setDescription(productDescription);
        setPrice(price);
        setSmartRestaurantManager(smartRestaurantManager);
    }

    void remove() {
        removeObject();
        deleteDomainObject();
    }


    void removeObject(){
        setName(null);
        setDescription(null);
        setSmartRestaurantManager(null);
    }
    
}
