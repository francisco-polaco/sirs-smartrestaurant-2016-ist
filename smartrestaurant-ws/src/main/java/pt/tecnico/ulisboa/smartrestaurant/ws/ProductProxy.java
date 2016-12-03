package pt.tecnico.ulisboa.smartrestaurant.ws;

import pt.tecnico.ulisboa.smartrestaurant.domain.Product;

/**
 * Created by francisco on 14/11/2016.
 */
public class ProductProxy {
    private String name;
    private String description;
    private double price;

   /* public ProductProxy(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }*/

    public ProductProxy(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    public String getName() {
        return name;
    }

    /*public void setName(String name) {
        this.name = name;
    }*/

    public String getDescription() {
        return description;
    }

//    public void setDescription(String description) {
//        this.description = description;
//    }

    public double getPrice() {
        return price;
    }

//    public void setPrice(double price) {
//        this.price = price;
//    }

    @Override
    public String toString() {
        return "ProductProxy{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
