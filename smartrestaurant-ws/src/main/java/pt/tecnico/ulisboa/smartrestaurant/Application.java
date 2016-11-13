package pt.tecnico.ulisboa.smartrestaurant;

import pt.tecnico.ulisboa.smartrestaurant.domain.ServerImpl;

import javax.xml.ws.Endpoint;

/**
 * Created by franc on 13/11/2016.
 */
public class Application {
    public static void main(String[] args){
        // Check arguments
        if (args.length != 1) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s wsURL%n", Application.class.getName());
            return;
        }
        String url = args[0];

        Endpoint endpoint = null;
        try {
            endpoint = Endpoint.create(new ServerImpl());

            // publish endpoint
            System.out.printf("Starting %s%n", url);
            endpoint.publish(url);

            // wait
            System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
            System.in.read();

        }catch (Exception e) {
            System.out.printf("Caught exception: %s%n", e);
            e.printStackTrace();

        } finally {
            try {
                if (endpoint != null) {
                    // stop endpoint
                    endpoint.stop();
                    System.out.printf("Stopped %s%n", url);
                }
            } catch (Exception e) {
                System.out.printf("Caught exception when stopping: %s%n", e);
            }
        }
    }
}
