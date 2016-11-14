package pt.tecnico.ulisboa.smartrestaurant.domain;

public class Order extends Order_Base {
    
    public Order() {
        super();
    }

    public Order(long id) {
        setId(id);
        setState(false);
    }

}
