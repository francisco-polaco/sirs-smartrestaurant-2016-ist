package pt.tecnico.ulisboa.smartrestaurant.waiter;

import pt.tecnico.ulisboa.smartrestaurant.waiter.ws.WaiterRealClientPort;

import java.util.Scanner;

/**
 * Created by francisco on 03/12/2016.
 */
public class WaiterClientApplication {

    public static void main(String[] args) throws Exception {
        System.out.println(WaiterClientApplication.class.getSimpleName() + " starting...");

        if (args.length != 1) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s uddiURL %n", WaiterClientApplication.class.getName());
            return;
        }
        System.out.println("cenas " + args[0]);

        WaiterRealClientPort port = new WaiterRealClientPort(args[0]);

        System.out.println(port.ping("no puedo"));
        System.in.read();


    }
}
