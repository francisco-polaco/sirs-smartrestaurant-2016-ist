package pt.tecnico.ulisboa.smartrestaurant;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.smartrestaurant.domain.DomainFacade;
import pt.tecnico.ulisboa.smartrestaurant.exception.OrderAlreadyRequestedException;
import pt.tecnico.ulisboa.smartrestaurant.exception.UserAlreadyExistsException;
import pt.tecnico.ulisboa.smartrestaurant.handler.SmartRestaurantOutboundHandler;
import pt.tecnico.ulisboa.smartrestaurant.ws.*;

import javax.xml.ws.Endpoint;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by franc on 13/11/2016.
 */
public class Application {
    public static void main(String[] args){
        // Check arguments
        if (args.length != 5) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s wsOrderURL wsWaiterURL wsKitchenURL wsWaiterReal%n", Application.class.getName());
            return;
        }

        String[] urls = { args[0], args[1], args[2]};

        WaiterProxy.endpointAddress = args[3];
        KitchenProxy.endpointAddress = args[4];

        SmartRestaurantOutboundHandler.handlerConstants.SENDER_SERVICE_NAME = "OrderServer";

        ArrayList<Endpoint> endpoints = new ArrayList<>();
        try {
            endpoints.add(Endpoint.create(new OrderServerImpl()));
            endpoints.add(Endpoint.create(new WaiterSoftServerImpl()));
            endpoints.add(Endpoint.create(new KitchenServerImpl()));

            // publish endpoint
            for(int i = 0 ; i < endpoints.size() ; i++) {
                System.out.printf("Starting %s%n", urls[i]);
                endpoints.get(i).publish(urls[i]);
            }
            //interaction();

            // wait
            System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
            System.in.read();

        }catch (Exception e) {
            System.out.printf("Caught exception: %s%n", e);
            e.printStackTrace();

        } finally {
            try {
                for(int i = 0 ; i < endpoints.size() ; i++) {
                    if (endpoints.get(i) != null) {
                        // stop endpoint
                        endpoints.get(i).stop();
                        System.out.printf("Stopped %s%n", urls[i]);
                    }
                }
            } catch (Exception e) {
                System.out.printf("Caught exception when stopping: %s%n", e);
            }
            FenixFramework.shutdown();
        }
    }
    // private static void interaction() throws NoSuchAlgorithmException {
    //     //Thread.sleep(2000);
    //     try {
    //         MessageDigest digest = MessageDigest.getInstance("SHA-256");
    //         byte[] password = digest.digest("initialize".getBytes(StandardCharsets.UTF_8));
    //         String password1 = "batata";
    //         String username = "francisco";
    //         try {
    //             DomainFacade.getInstance().registerNewUser(username, password1, "Francisco", "Santos", 100);
    //         } catch (UserAlreadyExistsException e) {
    //             System.out.println(e.getMessage());
    //         }
    //         byte[] sessionId = DomainFacade.getInstance().login(username1, password, 1);
    //         try {
    //             DomainFacade.getInstance().addProductToOrder(sessionId, "Bife da Vazia");
    //         } catch (OrderAlreadyRequestedException e) {
    //             System.out.println(e.getMessage());
    //         }
    //         String products = DomainFacade.getInstance().requestMyOrdersProducts(sessionId);
    //         System.out.println(products);
    //         DomainFacade.getInstance().orderProducts(sessionId, password);
    //         System.out.println("You owe: " + DomainFacade.getInstance().getPaymentDetails(sessionId) + "€");
    //         DomainFacade.getInstance().confirmPayment(sessionId, password, "ola");
    //         byte[] sessionId2 = DomainFacade.getInstance().login(username, password, 1);
    //     }catch (Exception e){
    //         System.out.println(e.getMessage());
    //         e.printStackTrace();
    //     }
    // }
}
