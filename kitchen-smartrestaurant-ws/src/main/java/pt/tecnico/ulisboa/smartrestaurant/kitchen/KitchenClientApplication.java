package pt.tecnico.ulisboa.smartrestaurant.kitchen;

import pt.tecnico.ulisboa.smartrestaurant.kitchen.ws.cli.KitchenClient;

import java.util.InputMismatchException;
import java.util.Scanner;

public class KitchenClientApplication {

    public static void main(String[] args) throws Exception {
        System.out.println(KitchenClientApplication.class.getSimpleName() + " starting...");

        if (args.length != 1) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s uddiURL name%n", KitchenClientApplication.class.getName());
            return;
        }
        System.out.println("cenas " + args[0]);


        KitchenClient costumer = new KitchenClient(args[0]);
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.print("\n\nMenu\n" +
                    "0 - Exit\n" +
                    "1 - Ping\n" +
                    "2 - Finished order\n" +
                    "\n>");
            int option;
            try{
                option=scanner.nextInt();
            }catch (InputMismatchException e ){
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
                        String ping = costumer.ping(pingMessage);
                        System.out.println(ping);
                    }catch (InputMismatchException e){
                        System.err.println("Não pode conter \ns");
                        break;
                    }
                    break;

                case 2:
                    System.out.print("Finished order\nIntroduza nr da order:\n>");
                    try{
                        int orderNr = scanner.nextInt();
                        scanner.nextLine();
                        String statusOrderNr = costumer.setOrderReadyToDeliver(orderNr);
                    }catch (InputMismatchException e){
                        System.err.println("Não pode conter \ns");
                        break;
                    }
                    break;


                default:
                    System.out.println("No puedo");
                    break;
            }

        }

    }
}