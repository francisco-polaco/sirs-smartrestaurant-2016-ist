package pt.tecnico.ulisboa.smartrestaurant.waiter;

import pt.tecnico.ulisboa.smartrestaurant.waiter.ws.WaiterRealClientPort;
import pt.tecnico.ulisboa.smartrestaurant.waiter.ws.WaiterRealServerImpl;

import javax.xml.ws.Endpoint;
import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by francisco on 03/12/2016.
 */
public class WaiterApplication {

    public static void main(String[] args) throws Exception {
        System.out.println(WaiterApplication.class.getSimpleName() + " starting...");

        if (args.length != 2) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s orderurl url %n", WaiterApplication.class.getName());
            return;
        }
        WaiterRealServerImpl server = new WaiterRealServerImpl();

        Endpoint waiterS = Endpoint.create(server);
        System.out.println(args[1]);
        waiterS.publish(args[1]);

        System.out.println("URL published: " + args[0]);
        System.out.println("Awaiting connections");

        WaiterRealClientPort port = new WaiterRealClientPort(args[0]);

        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("\n\nMenu\n" +
                    "0 - Exit\n" +
                    "1 - Ping\n" +
                    "2 - Deliver Order\n" +
                    "3 - Check Order Number\n" +
                    "> ");
            int option;
            try{
                option=scanner.nextInt();
            }catch (InputMismatchException e ){
                scanner.nextLine();
                System.err.println("Não é um número");
                continue;
            }
            scanner.nextLine();


            if(option == 0){
                System.out.println("A sair...");
                break;
            }
            switch (option){
                case 1:
                    System.out.print("Ping\nIntroduza mensagem de ping:\n>");
                    try{
                        String pingMessage = scanner.nextLine();
                        String ping = port.ping(pingMessage);
                        System.out.println(ping);
                    }catch (InputMismatchException e){
                        System.err.println("Não pode conter new lines");
                        break;
                    }
                    break;

                case 2:
                    try{
                        port.setOrderToDelivered(server.getOrderIdToDeliver().longValue());
                        server.setOrderIdToDeliver(null);
                    }catch (IllegalStateException e){
                        System.err.println(e.getMessage());
                        break;
                    }
                    System.out.println("Order delivered");
                    break;
                case 3:
                    try{
                        System.out.println("You need to deliver order #" + server.getOrderIdToDeliver().longValue());
                    }catch (IllegalStateException e){
                        System.err.println(e.getMessage());
                        break;
                    }
                    break;

                default:
                    System.out.println("No puedo");
                    break;
            }

        }

        try {
            waiterS.stop();
            System.out.printf("Stopped %s%n", args[1]);
        } catch (Exception e) {
            System.out.printf("Caught exception when stopping: %s%n", e);
        }
    }


}
