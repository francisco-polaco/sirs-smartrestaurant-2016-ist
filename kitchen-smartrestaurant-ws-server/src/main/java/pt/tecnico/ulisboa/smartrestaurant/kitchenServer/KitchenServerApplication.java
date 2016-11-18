package pt.tecnico.ulisboa.smartrestaurant.kitchenServer;

import javax.xml.ws.Endpoint;
import java.util.InputMismatchException;
import java.util.Scanner;

import pt.tecnico.ulisboa.smartrestaurant.kitchenServer.ws.cli.KitchenClientServerImpl;

public class KitchenServerApplication {

    public static void main(String[] args) throws Exception {
        System.out.println(KitchenServerApplication.class.getSimpleName() + " starting...");

        if (args.length != 1) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s uddiURL name%n", KitchenServerApplication.class.getName());
            return;
        }

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