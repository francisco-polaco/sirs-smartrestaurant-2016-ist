package pt.tecnico.ulisboa.smartrestaurant.cli;


import pt.tecnico.ulisboa.smartrestaurant.ws.OrderServer;
import pt.tecnico.ulisboa.smartrestaurant.ws.OrderServerImplService;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

/**
 * Created by francisco on 13/11/2016.
 */
public class Application {

    public static void main(String[] args) throws Exception {
        // Check arguments
        if (args.length != 1) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s wsURL%n", Application.class.getName());
            return;
        }

        String endpointAddress = args[0];

        if (endpointAddress == null) {
            System.out.println("Not found!");
            return;
        } else {
            System.out.printf("Found %s%n", endpointAddress);
        }

        System.out.println("Creating stub ...");
        // Copiado do tutorial do Indiano
        URL url = new URL(endpointAddress);
        QName qname = new QName("http://ws.mkyong.com/", "HelloWorldImplService");
        Service service = Service.create(url, qname);

        //OrderServerImplService service = new OrderServerImplService();
        OrderServer port = service.getPort(OrderServer.class);

        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
        //requestContext.put(JAXWSProperties.SSL_SOCKET_FACTORY, batata().getServerSocketFactory());

        System.out.println("Remote call ...");
        String result = port.ping("Ping!");
        System.out.println(result);
        System.in.read();
    }

   /* public static SSLContext batata() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        KeyManagerFactory kmf =
                KeyManagerFactory.getInstance( KeyManagerFactory.getDefaultAlgorithm() );

        KeyStore ks = KeyStore.getInstance( KeyStore.getDefaultType() );
        ks.load(new FileInputStream( certPath ), certPasswd.toCharArray() );

        kmf.init( ks, certPasswd.toCharArray() );

        sc.init( kmf.getKeyManagers(), null, null );
        return sc;
    }*/
}
