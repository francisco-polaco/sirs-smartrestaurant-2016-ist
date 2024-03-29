package pt.tecnico.ulisboa.smartrestaurant.ca.ws.exception;

/**
 * Created by xxlxpto on 08-05-2016.
 */
public class CertificateDoesntExistsException extends RuntimeException{

    private String mEntity;

    public CertificateDoesntExistsException(String entity){
        mEntity = entity;
    }

    @Override
    public String getMessage(){
        return "CA does not have the certificate of " + mEntity;
    }
}
