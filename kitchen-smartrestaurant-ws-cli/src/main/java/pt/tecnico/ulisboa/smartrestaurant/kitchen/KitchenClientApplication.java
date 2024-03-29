package pt.tecnico.ulisboa.smartrestaurant.kitchen;

import pt.tecnico.ulisboa.smartrestaurant.kitchen.ws.cli.KitchenClientImpl;

import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class KitchenClientApplication {

    public static void main(String[] args) throws Exception {
        System.out.println(KitchenClientApplication.class.getSimpleName() + " starting...");

        if (args.length != 2) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s uddiURL name%n", KitchenClientApplication.class.getName());
            return;
        }
        System.out.println("cenas " + args[0]);
        System.out.println("cenas " + args[1]);

        KitchenClientImpl kitchen = new KitchenClientImpl(args[0]);

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
                        String ping = kitchen.ping(pingMessage);
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
                        System.out.println(kitchen.setOrderReadyToDeliver(orderNr));
                    }catch (InputMismatchException e){
                        scanner.nextLine();
                        System.err.println("Não é um número");
                        break;
                    }
                    break;

                case 3:
                    System.out.println("List Orders");
                    //ArrayList<Long> lista = kitchen.getList();
                    /*for(Long l : lista){
                        System.out.println(l);

                    }*/
                    break;

                default:
                    System.out.println("No puedo");
                    break;
            }

        }

    }
}