package pt.tecnico.ulisboa.smartrestaurant.ca.ws;

import pt.tecnico.ulisboa.smartrestaurant.ca.ws.exception.CertificateDoesntExists;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by xxlxpto on 06-05-2016.
 */
@WebService
public interface CA {
    @WebMethod byte[] getEntityCertificate(String entity) throws CertificateDoesntExists;

}
