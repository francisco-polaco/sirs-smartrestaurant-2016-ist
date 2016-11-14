package pt.tecnico.ulisboa.smartrestaurant.domain;

import org.joda.time.DateTime;

public class Session extends Session_Base {
    
    public Session() {
        super();
    }

    Session(byte[] sessionId, int tableNo){
        setSessionId(sessionId);
        super.setTable(tableNo);
        super.setLoginTime(new DateTime());
    }

    void remove() {
        removeObject();
        deleteDomainObject();
    }


    void removeObject(){
        setSessionId(null);
        setLoginTime(null);
        setUser(null);
        setTable(null);
    }
    
}
