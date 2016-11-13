package pt.tecnico.ulisboa.smartrestaurant.cli;

import pt.tecnico.ulisboa.smartrestaurant.domain.Server;
import pt.tecnico.ulisboa.smartrestaurant.domain.ServerImplService;

import javax.xml.ws.BindingProvider;
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
        ServerImplService service = new ServerImplService();
        Server port = service.getServerImplPort();

        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

        System.out.println("Remote call ...");
        String result = port.ping("Ping!");
        System.out.println(result);
        System.in.read();
    }
}
