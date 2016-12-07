package pt.tecnico.ulisboa.smartrestaurant.kitchen;

import pt.tecnico.ulisboa.smartrestaurant.handler.SmartRestaurantHandler;
import pt.tecnico.ulisboa.smartrestaurant.kitchen.ws.cli.KitchenClientImpl;
import pt.tecnico.ulisboa.smartrestaurant.kitchen.ws.cli.KitchenClientServerImpl;

import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by francisco on 04/12/2016.
 */
public class KitchenApplication {
    public static void main(String[] args) throws Exception {
        System.out.println(KitchenApplication.class.getSimpleName() + " starting...");

        if (args.length != 2) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s orderurl url %n", KitchenApplication.class.getName());
            return;
        }
        try{
        KitchenClientServerImpl server = new KitchenClientServerImpl();

        Endpoint kitchenS = Endpoint.create(server);
        System.out.println(args[1]);
        kitchenS.publish(args[1]);

        System.out.println("URL published: " + args[1]);
        System.out.println("Awaiting connections");


        KitchenClientImpl port = new KitchenClientImpl(args[0]);
        System.out.println("Connected to: " + args[0]);

        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.print("\n\nMenu\n" +
                    "0 - Exit\n" +
                    "1 - Ping\n" +
                    "2 - Finished order\n" +
                    "3 - ListOrders\n" +
                    "\n>");
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
                    System.out.print("Finished order\nIntroduza nr da order:\n>");
                    try{
                        long orderNr = scanner.nextLong();
                        scanner.nextLine();
                        System.out.println(port.setOrderReadyToDeliver(orderNr));
                        server.removeList(orderNr);
                    }catch (InputMismatchException e){
                        scanner.nextLine();
                        System.err.println("Não é um número");
                        break;
                    }
                    break;

                case 3:
                    System.out.println("List Orders");
                    ArrayList<Long> lista = server.getList();
                    for(Long l : lista){
                        System.out.println(l);
                    }
                    if(lista.size() == 0){
                      System.out.println("No orders to prepare.");
                    }
                    break;

                default:
                    System.out.println("No puedo");
                    break;
            }

        }

        kitchenS.stop();
        System.out.printf("Stopped %s%n", args[1]);
        } catch (Exception e) {
            System.out.printf("Caught exception when stopping: %s%n", e);
        }

    }
}
