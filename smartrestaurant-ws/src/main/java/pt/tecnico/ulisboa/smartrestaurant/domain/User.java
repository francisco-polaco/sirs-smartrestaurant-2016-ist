package pt.tecnico.ulisboa.smartrestaurant.domain;

import org.joda.time.DateTime;
import pt.tecnico.ulisboa.smartrestaurant.exception.BruteForceAttemptException;

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
        super.setNumberOfFailedLogins(0);
    }

    void incrementFailedLoginAttempts(){
        setNumberOfFailedLogins(getNumberOfFailedLogins() + 1);
        if(getNumberOfFailedLogins() >= 10) {
            setNumberOfFailedLoginsReachedTime(new DateTime());
            throw new BruteForceAttemptException();
        }
    }
    void checkLoginTimeout(){
        if(getNumberOfFailedLoginsReachedTime() != null) {
            if (new DateTime().getMillis() - getNumberOfFailedLoginsReachedTime().getMillis() > 600000) {
                setNumberOfFailedLoginsReachedTime(null);
                setNumberOfFailedLogins(0);
            }else
                throw new BruteForceAttemptException();
        }
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
        setNumberOfFailedLoginsReachedTime(null);
    }


}
