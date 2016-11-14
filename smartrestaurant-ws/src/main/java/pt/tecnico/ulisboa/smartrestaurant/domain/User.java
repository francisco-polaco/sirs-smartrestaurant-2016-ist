package pt.tecnico.ulisboa.smartrestaurant.domain;

public class User extends User_Base {


    public User() {
        super();
    }

    User(String username, byte[] hashedPassword, String firstname, String lastName, int nif, SmartRestaurantManager manager) {
        super.setUsername(username);
        super.setPassword(hashedPassword);
        super.setFirstName(firstname);
        super.setLastName(lastName);
        super.setNif(nif);
        super.setSmartRestaurantManager(manager);
    }

    void remove() {
        removeObject();
        deleteDomainObject();
    }


    void removeObject(){
        setUsername(null);
        setOrder(null);
        setSession(null);
        setFirstName(null);
        setLastName(null);
        setSmartRestaurantManager(null);
        setPassword(null);
    }


}
