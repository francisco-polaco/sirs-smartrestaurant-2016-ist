package pt.tecnico.ulisboa.smartrestaurant.handler;

/**
 * Created by xxlxpto on 07-05-2016.
 */
public class HandlerConstants {
     final String CERTIFICATE_EXTENSION = ".cer";
     final String KEYSTORE_EXTENSION = ".jks";
     final String CONTEXT_PROPERTY = "my.property";
     final String SIG_ELEMENT_NAME = "signature";
     final String SENDER_ELEMENT_NAME = "sender";
     final String PREFIX = "S";
     final String NAMESPACE = "pt.tecnico.ulisboa";
     public String SENDER_SERVICE_NAME = "OrderServer";
     public String RCPT_SERVICE_NAME = "KitchenServer";
     final String KEYSTORE_PASSWORD = "ins3cur3";
     final String KEY_PASSWORD = "1nsecure";
     final String TIMESTAMP = "timestamp";
     final String NONCE = "secure_random";
     final String AES_KEY_FILE = "extras/aes_key.jks";
     final String AES_KEY_FILE_PASSWORD = "sirs_insecure";

}
