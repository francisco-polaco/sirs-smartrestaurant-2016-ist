package pt.tecnico.ulisboa.smartrestaurant.domain;

public class Product extends Product_Base {
    
    public Product() {
        super();
    }

    Product(String productName, String productDescription, double price){
        setName(productName);
        setDescription(productDescription);
        setPrice(price);
    }
    
}
