package pt.tecnico.ulisboa.smartrestaurant;

import pt.tecnico.ulisboa.smartrestaurant.ws.KitchenServerImpl;
import pt.tecnico.ulisboa.smartrestaurant.ws.OrderServerImpl;
import pt.tecnico.ulisboa.smartrestaurant.ws.WaiterServerImpl;

import javax.xml.ws.Endpoint;
import java.util.ArrayList;

/**
 * Created by franc on 13/11/2016.
 */
public class Application {
    public static void main(String[] args){
        // Check arguments
        if (args.length != 3) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s wsOrderURL wsWaiterURL wsKitchenURL%n", Application.class.getName());
            return;
        }

        String[] urls = { args[0], args[1], args[2]};

        ArrayList<Endpoint> endpoints = new ArrayList<>();
        try {
            endpoints.add(Endpoint.create(new OrderServerImpl()));
            endpoints.add(Endpoint.create(new WaiterServerImpl()));
            endpoints.add(Endpoint.create(new KitchenServerImpl()));

            // publish endpoint
            for(int i = 0 ; i < endpoints.size() ; i++) {
                System.out.printf("Starting %s%n", urls[i]);
                endpoints.get(i).publish(urls[i]);
            }
            // wait
            System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
            System.in.read();

        }catch (Exception e) {
            System.out.printf("Caught exception: %s%n", e);
            e.printStackTrace();

        } finally {
            try {
                for(int i = 0 ; i < endpoints.size() ; i++) {
                    if (endpoints.get(i) != null) {
                        // stop endpoint
                        endpoints.get(i).stop();
                        System.out.printf("Stopped %s%n", urls[i]);
                    }
                }
            } catch (Exception e) {
                System.out.printf("Caught exception when stopping: %s%n", e);
            }
        }
    }
}
