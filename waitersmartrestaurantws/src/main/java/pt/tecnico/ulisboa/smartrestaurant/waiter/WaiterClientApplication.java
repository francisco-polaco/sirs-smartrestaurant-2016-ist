package pt.tecnico.ulisboa.smartrestaurant.waiter;

import pt.tecnico.ulisboa.smartrestaurant.waiter.ws.WaiterClientPort;

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

        WaiterClientPort port = new WaiterClientPort(args[0]);

        System.out.println(port.ping());
        System.in.read();
        Scanner scanner = new Scanner(System.in);

        /*while(true){
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
                    System.out.println(kitchen.listAllOrders());
                    break;

                default:
                    System.out.println("No puedo");
                    break;
            }

        }*/

    }
}
