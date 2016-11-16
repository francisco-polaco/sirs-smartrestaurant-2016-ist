package pt.tecnico.ulisboa.smartrestaurant.costumer;

import pt.tecnico.ulisboa.smartrestaurant.costumer.ws.cli.CostumerClient;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CostumerClientApplication {

    public static void main(String[] args) throws Exception {
        System.out.println(CostumerClientApplication.class.getSimpleName() + " starting...");

        if (args.length != 1) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s uddiURL name%n", CostumerClientApplication.class.getName());
            return;
        }
        System.out.println("cenas " + args[0]);


        CostumerClient costumer = new CostumerClient(args[0]);
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.print("\n\nMenu\n" +
                    "0 - Exit\n" +
                    "1 - Ping\n" +
                    "2 - Login\n" +
                    "3 - List Products\n" +
                    "4 - Add Product\n" +
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
                        String ping = costumer.ping(pingMessage);
                        System.out.println(ping);
                    }catch (InputMismatchException e){
                        System.err.println("Não pode conter \ns");
                        break;
                    }
                    break;

                case 2:
                    System.out.println("Login");
                    System.out.print("Introduza username:\n>");
                    String username = scanner.nextLine();
                    System.out.print("Introduza password:\n>");
                    String password = scanner.nextLine();
                    System.out.print("Leia o QR code da sua mesa:\n>");
                    scanner.nextLine();
                    int tableNo = costumer.readQRCode();
                    System.out.println(costumer.login(username, password, tableNo));
                    break;

                case 3:
                    System.out.println("List Products");
                    System.out.println(costumer.requestMyOrderProducts());
                    break;

                case 4:
                    System.out.println("Add Product");
                    System.out.print("Introduza nome do prato:\n>");
                    String meal = scanner.nextLine();
                    System.out.println(costumer.addProductToOrder(meal));
                    break;

                default:
                    System.out.println("No puedo");
                    break;
            }

        }

    }
}