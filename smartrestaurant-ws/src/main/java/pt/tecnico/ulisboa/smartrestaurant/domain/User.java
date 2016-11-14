package pt.tecnico.ulisboa.smartrestaurant.domain;

import org.joda.time.DateTime;
import pt.tecnico.ulisboa.smartrestaurant.exception.SessionExpiredException;


public class User extends User_Base {

    private static final int TIMEOUT_SESSION_TIME = 1800;     // half an hour


    public User() {
        super();
    }

    public User(String username, byte[] hashedPassword, String firstname, String lastName, int nif, SmartRestaurantManager manager) {
        super.setUsername(username);
        super.setPassword(hashedPassword);
        super.setFirstName(firstname);
        super.setLastName(lastName);
        super.setNif(nif);
        super.setSmartRestaurantManager(manager);
    }




}
