package pt.tecnico.ulisboa.smartrestaurant.ca;

import pt.tecnico.ulisboa.smartrestaurant.ca.ws.cli.CAClient;

import java.io.IOException;

/**
 * Created by xxlxpto on 06-05-2016.
 */
public class CAClientApplication {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s wsURL%n",
                    pt.tecnico.ulisboa.smartrestaurant.ca.CAClientApplication.class.getName());
            return;
        }


        // Start CA
        CAClient CAClient = new CAClient(args[0]);
        System.in.read();
        //CAClient.getEntityCertificate("Broker");
    }
}
