package pt.tecnico.ulisboa.smartrestaurant.domain;

import org.joda.time.DateTime;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.smartrestaurant.exception.*;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SmartRestaurantManager extends SmartRestaurantManager_Base {

    private static final int TIMEOUT_SESSION_TIME = 1800;     // half an hour

    private SmartRestaurantManager() {
        FenixFramework.getDomainRoot().setSmartRestaurantManager(this);
        addProduct(new Product("Bife da Vazia", "Um soculento bife da Vazia que o irá fazer chorar por mais.", 12.99, this));
        addProduct(new Product("Bacalhau à Lagareiro", "Experimente o que mais de Português " +
                "nós temos, este bacalhau irá dar-lhe a volta à cabeça", 10.99, this));
        addProduct(new Product("Cozido à Portuguesa", "O nome diz tudo!", 20.59, this));
        addProduct(new Product("Francesinha", "Prove um prato típico do Norte do País, carago!", 7.56, this)); //TODO change this
        addProduct(new Product("Sopa de Peixe", "Porque a sopa faz bem a todos!", 7.56, this));
        addProduct(new Product("Leite Creme", "Experimente este belo leite creme queimado na hora!", 7.56, this));

    }

    public static SmartRestaurantManager getInstance(){
        SmartRestaurantManager mngr = FenixFramework.getDomainRoot().getSmartRestaurantManager();
        if (mngr != null)
            return mngr;

        System.out.println("New Manager");

        return new SmartRestaurantManager();
    }

    // Package methods to ensure that "they" only access Facade.
    // Who's "they" you may ask, the Illuminati ofc
    void registerNewUser(String username, byte[] hashedPassword, String firstName, String lastName, int nif)
            throws UserAlreadyExistsException {
        System.out.println(username + " is being registered.");

        canICreateAnUser(username);
        super.addUser(new User(username, hashedPassword, firstName, lastName, nif, this));
    }

    byte[] login(String username, byte[] hashedPassword, int tableNo) throws NoSuchAlgorithmException {
        System.out.println(username + " is logging in...");

        User user;
        byte[] hashToken;
        user = getUserByUsername(username);
        checkAndLogoutUser(user);
        if(Arrays.equals(user.getPassword(), hashedPassword)) {
            if (user.getSession() != null) {
                System.out.println(username + " is logged in.");
                return user.getSession().getSessionId();
            } else {
                System.out.println(username + " session timed out, creating a new session.");
                hashToken = generateToken();
                Session s = new Session(hashToken, tableNo);
                s.setUser(user);
                user.setSession(s);
                System.out.println(username + " is logged in.");
                return hashToken;
            }
        }else
            throw new AccessDeniedException();
    }

    void addRequestToOrder(byte[] sessionId, String productName){
        Product product = getProductByName(productName);
        if(product == null) throw new ProductDoesntExistException(productName);
        else {
            User u = getUserBySessionId(sessionId);
            if(u.getOrder() == null){ // if there is not an active order
                Order order = new Order(generateOrderId(), u);
                order.addProduct(product);
                order.setUser(u);
                u.setOrder(order);
            }else
                u.getOrder().addProduct(product);
            System.out.println("Adding " + productName + " to " + u.getFirstName() + " order.");
        }
    }

    List<Product> requestMyOrdersProducts(byte[] sessionId){
        User u = getUserBySessionId(sessionId);
        List<Product> productList = new ArrayList<>();
        if(u.getOrder() != null) {
            for (Product p : u.getOrder().getProductSet()) {
                productList.add(p);
            }
        }
        return productList;
    }

    void orderProducts(byte[] sessionId, byte[] hashedPassword){
        User u = getUserBySessionId(sessionId);
        checkAndLogoutUser(u);
        if(u.getSession() != null){
            if(Arrays.equals(u.getPassword(), hashedPassword)){
                // Para alem do session id, a password esta correta. Logo, devera ser o utilizador
                System.out.println(u.getUsername() + " is ordering his order.");
                Order toRequest = u.getOrder();
                toRequest.setState(1); // set state meaning that the request is at the kitchen
                // send order to kitchen
            }else{

                throw new AccessDeniedException();
            }
        }else
            throw new SessionExpiredException(u.getUsername());
    }

    void setOrderReadyToDeliver(long orderId){
        setOrderToState(orderId, 2);
    }

    void setOrderToDelivered(long orderId){
        setOrderToState(orderId, 3);
    }

    void setOrderToPayed(long orderId){
        for (User u : getUserSet()) {
            if (u.getOrder() != null && u.getOrder().getId() == orderId) {
                u.getOrder().remove();
                u.setOrder(null);
                break;
            } else
                throw new OrderDoesntExistException();
        }
    }

    // Private methods
    private void canICreateAnUser(String username){
        try{
            getUserByUsername(username);
        }catch (UserDoesntExistException e){
            return;
        }
        throw new UserAlreadyExistsException();
    }

    private User getUserByUsername(String username){
        for(User u : getUserSet()){
            if(u.getUsername().equals(username)) return u;
        }
        throw new UserDoesntExistException();
    }

    private User getUserBySessionId(byte[] sessionId){
        for(User u : getUserSet()){
            checkAndLogoutUser(u);
            if(u.getSession() != null && Arrays.equals(u.getSession().getSessionId(), sessionId)) return u;
        }
        throw new UserWithSessionIdDoenstExistException();
    }

    private Product getProductByName(String name){
        for(Product p : getProductSet()){
            if(p.getName().equals(name)) return p;
        }
        return null;
    }

    private byte[] generateToken() throws NoSuchAlgorithmException {
        boolean notFound = true;
        long token;
        byte[] tokenHash = null;
        while(notFound) {
            // Integers can be negative, so to assure unicity for tests we only consider the positive ones
            long randomLong = new BigInteger(64, new Random()).longValue();
            token = randomLong < 0 ? -randomLong : randomLong;
            // Hashing
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            tokenHash = digest.digest(Long.toString(token).getBytes(StandardCharsets.UTF_8));

            notFound = false;
            for(User u : getUserSet()) {
                if(u.getSession() != null) {
                    checkAndLogoutUser(u);
                    if (u.getSession() != null && Arrays.equals(u.getSession().getSessionId(), tokenHash) && !notFound)
                        notFound = true;
                }
            }
        }
        return tokenHash;
    }

    private long generateOrderId()  {
        boolean notFound = true;
        long id = 0;
        while(notFound) {
            // Integers can be negative, so to assure unicity for tests we only consider the positive ones
            long randomLong = new BigInteger(64, new Random()).longValue();
            id = randomLong < 0 ? -randomLong : randomLong;
            notFound = false;
            for(User u : getUserSet()) {
                if(u.getOrder() != null) {
                    checkAndLogoutUser(u);
                    if (u.getOrder().getId() == id && !notFound)
                        notFound = true;
                }
            }
        }
        return id;
    }

    private void checkAndLogoutUser(User u) {
        if(new DateTime().getMillis() - u.getSession().getLoginTime().getMillis() > TIMEOUT_SESSION_TIME) {
            u.getSession().remove();
            u.setSession(null);
        }
    }

    private void setOrderToState(long orderId, int state) {
        for (User u : getUserSet()) {
            if (u.getOrder() != null && u.getOrder().getId() == orderId) {
                u.getOrder().setState(state);
                break;
            } else
                throw new OrderDoesntExistException();
        }
    }

}
