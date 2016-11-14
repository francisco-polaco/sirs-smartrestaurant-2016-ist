package pt.tecnico.ulisboa.smartrestaurant.domain;

import pt.tecnico.ulisboa.smartrestaurant.exception.OrderAlreadyRequestedException;

public class Order extends Order_Base {
    /*
    Possible States:
        0   -   Customer choosing products
        1   -   Order requested to the kitchen
        2   -   Order cooked
        3   -   Order delivered
    */
    public Order() {
        super();
    }

    Order(long id) {
        setId(id);
        setState(0);
    }

    @Override
    public void addProduct(Product product) {
        if(getState() == 0) super.addProduct(product);
        else throw new OrderAlreadyRequestedException();
    }
}
