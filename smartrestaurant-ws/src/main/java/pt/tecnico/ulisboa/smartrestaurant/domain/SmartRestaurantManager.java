package pt.tecnico.ulisboa.smartrestaurant.domain;

import pt.ist.fenixframework.FenixFramework;

public class SmartRestaurantManager extends SmartRestaurantManager_Base {
    
    private SmartRestaurantManager() {
        FenixFramework.getDomainRoot().setSmartRestaurantManager(this);
        /*super.setFilesystem(new FileSystem(this));
        currentSession = new Session(generateToken(), getFilesystem().getGuest(),
                getFilesystem().getGuest().getHomeDirectory(),this);
        addSession(currentSession);*/
        System.out.println("Connected to Fenix Framework");
    }

    public static SmartRestaurantManager getInstance(){
        SmartRestaurantManager mngr = FenixFramework.getDomainRoot().getSmartRestaurantManager();
        if (mngr != null)
            return mngr;

        System.out.println("New Manager");

        return new SmartRestaurantManager();
    }
}
