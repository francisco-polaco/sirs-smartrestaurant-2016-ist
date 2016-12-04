package pt.tecnico.ulisboa.smartrestaurant.customer;

import pt.tecnico.ulisboa.smartrestaurant.customer.ws.cli.CustomerClient;

import javax.sound.midi.SysexMessage;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.Console;

public class CustomerClientApplication {

    public static void main(String[] args) throws Exception {
        System.out.println(CustomerClientApplication.class.getSimpleName() + " starting...");

        if (args.length != 1) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s uddiURL name%n", CustomerClientApplication.class.getName());
            return;
        }
        System.out.println("cenas " + args[0]);


        CustomerClient customer = new CustomerClient(args[0]);
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        while(true){
            System.out.print("\n\nMenu\n" +
                    "0 - Exit\n" +
                    "1 - Ping\n" +
                    "2 - Login\n" +
                    "3 - List Products\n" +
                    "4 - Add Product\n" +
                    "5 - Register\n" +
                    "6 - Order Products\n" +
                    "7 - Confirm payment\n" +
                    "8 - Payment Details\n" +
                    "9 - Logout\n" +
                    "\n> ");
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
                //Ping
                case 1:
                    System.out.print("Ping\nIntroduza mensagem de ping:\n> ");
                    try{
                        String pingMessage = scanner.nextLine();
                        String ping = customer.ping(pingMessage);
                        System.out.println(ping);
                    }catch (InputMismatchException e){
                        System.err.println("Não pode conter \ns");
                        break;
                    }
                    break;

                //Login
                case 2:
                    System.out.println("Login");
                    System.out.print("Introduza username:\n> ");
                    String username = scanner.nextLine();
                    String password = new String(console.readPassword("Introduza password:\n> "));
                    System.out.print("Leia o QR code da sua mesa:\n> ");
                    scanner.nextLine();
                    int tableNo = customer.readQRCode();
                    System.out.println(customer.login(username, password, tableNo));
                    break;

                //List Products
                case 3:
                    customer.checkSessionExpired();
                    if(!customer.checkLogin()){
                        System.err.println("Need to login!");
                        break;
                    }
                    System.out.println("List Products");
                    System.out.println(customer.requestMyOrderProducts());
                    break;

                //
                case 4:
                    customer.checkSessionExpired();
                    if(!customer.checkLogin()){
                        System.err.println("Need to login!");
                        break;
                    }
                    System.out.println("Add Product");
                    System.out.print("Introduza nome do prato:\n> ");
                    String meal = scanner.nextLine();
                    System.out.println(customer.addProductToOrder(meal));
                    break;

                case 5:
                    if(customer.checkLogin()){
                        System.err.println("Already logged in!");
                        break;
                    }
                    while(true){
                        System.out.println("Register user");
                        System.out.print("Escolha um username:\n> ");
                        String regUsername = scanner.nextLine();
                        String regPassword = new String(console.readPassword("Introduza password:\n> "));
                        System.out.print("Qual o seu primeiro nome:\n> ");
                        String firstName = scanner.nextLine();
                        System.out.print("Qual o seu ultimo nome:\n> ");
                        String lastName = scanner.nextLine();
                        int nif;
                        while(true) {
                            try {
                                System.out.print("Qual o seu nif:\n> ");
                                nif= scanner.nextInt();
                                scanner.nextLine();

                            } catch (InputMismatchException e) {
                                scanner.nextLine();
                                System.err.println("O nif que inseriu nao é um número");
                                continue;
                            }
                            break;
                        }
                        if(regUsername.equals("") || regPassword.equals("") || firstName.equals("") || lastName.equals("")){
                            System.err.println("Campos nao podem ser vazios");
                            continue;
                        }
                        System.out.println(customer.registerNewUser(regUsername, regPassword, firstName, lastName, nif));
                        break;

                    }
                    break;

                case 6:
                    customer.checkSessionExpired();
                    if(!customer.checkLogin()){
                        System.err.println("Need to login!");
                        break;
                    }
                    System.out.println("Order Products");
                    String orderPassword = new String(console.readPassword("Introduza password:\n> "));
                    System.out.println(customer.orderProducts(orderPassword));
                    break;

                case 7:
                    customer.checkSessionExpired();
                    if(!customer.checkLogin()){
                        System.err.println("Need to login!");
                        break;
                    }
                    System.out.println("Confirm payment");
                    String paymentPassword = new String(console.readPassword("Introduza password:\n> "));
                    System.out.print("Referencia paypal:\n> ");
                    String refPaypal = scanner.nextLine();
                    System.out.println(customer.confirmPayment(paymentPassword, refPaypal));
                    break;

                case 8:
                    customer.checkSessionExpired();
                    if(!customer.checkLogin()){
                        System.err.println("Need to login!");
                        break;
                    }
                    System.out.println("Payment Details");
                    System.out.println(customer.getPaymentDetails());

                    break;

                case 9:
                    customer.checkSessionExpired();
                    if(!customer.checkLogin()){
                        System.err.println("Need to login!");
                        break;
                    }
                    System.out.println(customer.logOut());
                    break;

                default:
                    System.out.println("No puedo");
                    break;
            }

        }

    }
}