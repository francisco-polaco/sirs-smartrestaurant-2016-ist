package pt.tecnico.ulisboa.smartrestaurant.domain;

import pt.tecnico.ulisboa.smartrestaurant.exception.OrderAlreadyRequestedException;

public class Order extends Order_Base {
    /*
    Possible States:
        0   -   Customer choosing products
        1   -   Order requested to the kitchen
        2   -   Order cooked
        3   -   Order delivered
    Deleted -   Order payed
    */
    public Order() {
        super();
    }

    Order(long id, User user) {
        setId(id);
        setState(0);
        setUser(user);
    }

    @Override
    public void addProduct(Product product) {
        if(getState() == 0) super.addProduct(product);
        else throw new OrderAlreadyRequestedException();
    }

    void remove() {
        removeObject();
        deleteDomainObject();
    }


    void removeObject(){
        setUser(null);
        for(Product p : getProductSet()){
            removeProduct(p);
        }
    }

}
