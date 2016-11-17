package pt.tecnico.ulisboa.smartrestaurant.ca.ws.exception;

/**
 * Created by xxlxpto on 08-05-2016.
 */
public class CertificateIsInBlackListException extends RuntimeException{

    private String mEntity;

    public CertificateIsInBlackListException(String entity){
        mEntity = entity;
    }

    @Override
    public String getMessage(){
        return "CA reports that " + mEntity + " is blacklisted.";
    }
}
