package pt.tecnico.ulisboa.smartrestaurant.domain;

import org.joda.time.DateTime;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.smartrestaurant.exception.ProductDoesntExistException;
import pt.tecnico.ulisboa.smartrestaurant.exception.UserLoginAlreadyExistsException;
import pt.tecnico.ulisboa.smartrestaurant.exception.UserLoginDoesntExistException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class SmartRestaurantManager extends SmartRestaurantManager_Base {

    private static final int TIMEOUT_SESSION_TIME = 1800;     // half an hour

    private SmartRestaurantManager() {
        FenixFramework.getDomainRoot().setSmartRestaurantManager(this);
        /*super.setFilesystem(new FileSystem(this));
        currentSession = new Session(generateToken(), getFilesystem().getGuest(),
                getFilesystem().getGuest().getHomeDirectory(),this);
        addSession(currentSession);*/
        addProduct(new Product("Bife da Vazia", "Um soculento bife da Vazia que o irá fazer chorar por mais.", 12.99));
        addProduct(new Product("Bacalhau à Lagareiro", "Experimente o que mais de Português nós temos, este bacalhau irá dar-lhe a volta à cabeça", 10.99));
        addProduct(new Product("Cozido à Portuguesa", "O nome diz tudo!", 20.59));
        addProduct(new Product("Francesinha", "Prove um prato típico do Norte do País, carago!", 7.56)); //TODO change this
        addProduct(new Product("Sopa de Peixe", "Porque a sopa faz bem a todos!", 7.56));
        addProduct(new Product("Leite Creme", "Experimente este belo leite creme queimado na hora!", 7.56));

    }

    public static SmartRestaurantManager getInstance(){
        SmartRestaurantManager mngr = FenixFramework.getDomainRoot().getSmartRestaurantManager();
        if (mngr != null)
            return mngr;

        System.out.println("New Manager");

        return new SmartRestaurantManager();
    }


    void registerNewUser(String username, byte[] hashedPassword, String firstName, String lastName, int nif)
            throws UserLoginAlreadyExistsException{
        System.out.println(username + " is being registered.");

        if(getUserByUsername(username) == null)
            super.addUser(new User(username, hashedPassword, firstName, lastName, nif, this));
        else throw new UserLoginAlreadyExistsException();
    }

    byte[] login(String username, byte[] hashedPassword, int tableNo) throws NoSuchAlgorithmException {
        System.out.println(username + " is logging in...");

        User user;
        byte[] hashToken;
        if((user = getUserByUsername(username)) == null) throw new UserLoginDoesntExistException();
        else {
            hashToken = generateToken();
            Session s = new Session(hashToken, tableNo);
            s.setUser(user);
            user.setSession(s);
            System.out.println(username + " is logged in.");
            return hashToken;
        }
    }

    void addRequestToOrder(byte[] sessionId, String productName){
        Product product = getProductByName(productName);
        if(product == null) throw new ProductDoesntExistException(productName);
        else {
            User u = getUserBySessionId(sessionId);
            if(u.getOrder() == null){ // if there is not an active order
                Order order = new Order(generateOrderId());
                order.addProduct(product);
                u.setOrder(order);
            }else
                u.getOrder().addProduct(product);
            System.out.println("Adding " + productName + " to " + u.getFirstName() + " order.");
        }
    }

    private User getUserByUsername(String username){
        for(User u : getUserSet()){
            if(u.getUsername().equals(username)) return u;
        }
        return null;
    }

    private User getUserBySessionId(byte[] sessionId){
        for(User u : getUserSet()){
            checkAndLogoutUser(u);
            if(u.getSession() != null && Arrays.equals(u.getSession().getSessionId(), sessionId)) return u;
        }
        return null;
    }


    private Product getProductByName(String name){
        for(Product p : getProductSet()){
            if(p.getName().equals(name)) return p;
        }
        return null;
    }

    private byte[] generateToken() throws NoSuchAlgorithmException {
        boolean notFound = true;
        long token = 0;
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
        if(new DateTime().getMillis() - u.getSession().getLoginTime().getMillis() > TIMEOUT_SESSION_TIME)
            u.setSession(null);
    }

}
