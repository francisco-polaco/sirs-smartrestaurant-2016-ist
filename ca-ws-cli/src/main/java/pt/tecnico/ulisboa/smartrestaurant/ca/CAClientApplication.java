package pt.tecnico.ulisboa.smartrestaurant.ca;

import pt.tecnico.ulisboa.smartrestaurant.ca.ws.cli.CAClient;

/**
 * Created by xxlxpto on 06-05-2016.
 */
public class CAClientApplication {
    public static void main(String[] args){
        if (args.length < 1) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s wsURL%n",
                    pt.tecnico.ulisboa.smartrestaurant.ca.CAClientApplication.class.getName());
            return;
        }


        // Start CA
        CAClient CAClient = new CAClient();
        //CAClient.getEntityCertificate("Broker");
    }
}
