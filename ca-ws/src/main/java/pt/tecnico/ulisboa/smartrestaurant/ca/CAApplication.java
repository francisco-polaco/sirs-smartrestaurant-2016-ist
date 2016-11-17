package pt.tecnico.ulisboa.smartrestaurant.ca;

import pt.tecnico.ulisboa.smartrestaurant.ca.ws.CAImplementation;

import javax.xml.ws.Endpoint;
import java.util.Scanner;

/**
 * Created by xxlxpto on 06-05-2016.
 */
public class CAApplication {
    public static void main(String[] args){
        System.out.println("CA starting...");
        if (args.length < 1) {
            System.err.println("Argument missing!");
            System.err.printf("Usage: java %s wsURL%n", CAApplication.class.getName());
            return;
        }

        String url = args[0];

        Endpoint endpoint = null;
        try {
            CAImplementation ca = new CAImplementation();
            endpoint = Endpoint.create(ca);
            // publish endpoint
            System.out.printf("Starting %s%n", url);
            endpoint.publish(url);
            System.out.println("Awaiting connections");

            System.out.println("Welcome to CA terminal!");
            printHelp();
            Scanner scanner = new Scanner(System.in);
            String line;
            System.out.print("> ");
            while(!((line = scanner.nextLine()).equals("q"))){
                if(line.startsWith("blacklist ")){
                    String[] tokens = line.split(" ");
                    if(tokens.length == 2){
                        ca.addEntityToBlackList(tokens[1]);
                    }
                }else if (line.startsWith("h") && line.length() == 1) {
                    printHelp();
                }
                else{
                    System.err.println("Unknown Command! Type 'h' for help.");
                }
                System.out.print("> ");
            }
            System.out.println("Bye!");

        } catch (Exception e) {
            System.out.printf("Caught exception: %s%n", e);
            e.printStackTrace();

        } finally {
            try {
                if (endpoint != null) {
                    // stop endpoint
                    endpoint.stop();
                    System.out.printf("Stopped %s%n", url);
                }
            } catch (Exception e) {
                System.out.printf("Caught exception when stopping: %s%n", e);
            }
        }
    }

    private static void printHelp(){
        System.out.println("You have the following commands available:" +
                "\n\tblacklist <entity> - blacklists an entity" +
                "\n\th - shows this message" +
                "\n\tq - exits CA");
    }
}
