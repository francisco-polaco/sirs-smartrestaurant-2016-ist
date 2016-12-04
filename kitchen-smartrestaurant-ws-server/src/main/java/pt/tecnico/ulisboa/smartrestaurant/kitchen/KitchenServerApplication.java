package pt.tecnico.ulisboa.smartrestaurant.kitchen;

import javax.xml.ws.Endpoint;

import pt.tecnico.ulisboa.smartrestaurant.handler.SmartRestarantHandler;
import pt.tecnico.ulisboa.smartrestaurant.kitchen.ws.cli.KitchenClientServerImpl;

public class KitchenServerApplication {

    public static void main(String[] args) throws Exception {
        System.out.println(KitchenServerApplication.class.getSimpleName() + " starting...");

        if (args.length != 1) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s uddiURL name%n", KitchenServerApplication.class.getName());
            return;
        }

        SmartRestarantHandler.handlerConstants.SENDER_SERVICE_NAME = "KitchenServer";
        SmartRestarantHandler.handlerConstants.RCPT_SERVICE_NAME = "OrderServer";

        Endpoint kitchenS =Endpoint.create(new KitchenClientServerImpl());
        System.out.println(args[0]);
        kitchenS.publish(args[0]);

        System.out.println("Awaiting connections");
        System.out.println("Press enter to shutdown");
        System.in.read();

        try {
            kitchenS.stop();
            System.out.printf("Stopped %s%n", args[0]);
        } catch (Exception e) {
            System.out.printf("Caught exception when stopping: %s%n", e);
        }

    }
}