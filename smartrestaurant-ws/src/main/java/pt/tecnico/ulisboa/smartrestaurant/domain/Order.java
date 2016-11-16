package pt.tecnico.ulisboa.smartrestaurant.domain;

import pt.tecnico.ulisboa.smartrestaurant.exception.OrderAlreadyRequestedException;
import pt.tecnico.ulisboa.smartrestaurant.exception.OrderWasNotDeliveredYetException;
import pt.tecnico.ulisboa.smartrestaurant.exception.OrderWasNotRequestedYetException;

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

    @Override
    public void setState(int state) {
        if(state != getState() + 1 && state > 3 && state < 0) throw new OrderWasNotDeliveredYetException();
        super.setState(state);
    }

    void remove() {
        if (getState() != 3) throw new OrderWasNotDeliveredYetException();
        removeObject();
        deleteDomainObject();
    }


    void removeObject(){
        setUser(null);
        for(Product p : getProductSet()){
            removeProduct(p);
        }
    }

    double amountToPay(){
        if(getState() == 0) throw new OrderWasNotRequestedYetException();
        double result = 0.0;
        for(Product p : getProductSet()){
            result += p.getPrice();
        }
        return result;
    }

}
