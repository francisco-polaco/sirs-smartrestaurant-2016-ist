package pt.tecnico.ulisboa.smartrestaurant.waiter;

import pt.tecnico.ulisboa.smartrestaurant.waiter.ws.WaiterRealServerImpl;

import javax.xml.ws.Endpoint;

/**
 * Created by francisco on 03/12/2016.
 */
public class WaiterRealServerApplication {

    public static void main(String[] args) throws Exception {
        System.out.println(WaiterRealServerApplication.class.getSimpleName() + " starting...");

        if (args.length != 1) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s url %n", WaiterRealServerApplication.class.getName());
            return;
        }

        Endpoint waiterS = Endpoint.create(new WaiterRealServerImpl());
        System.out.println(args[0]);
        waiterS.publish(args[0]);

        System.out.println("URL published: " + args[0]);
        System.out.println("Awaiting connections");
        System.out.println("Press enter to shutdown");
        System.in.read();

        try {
            waiterS.stop();
            System.out.printf("Stopped %s%n", args[0]);
        } catch (Exception e) {
            System.out.printf("Caught exception when stopping: %s%n", e);
        }
    }
}
