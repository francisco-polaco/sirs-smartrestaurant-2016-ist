package pt.tecnico.ulisboa.smartrestaurant.domain;

import org.joda.time.DateTime;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.ulisboa.smartrestaurant.exception.*;
import pt.tecnico.ulisboa.smartrestaurant.ws.KitchenProxy;
import pt.tecnico.ulisboa.smartrestaurant.ws.WaiterProxy;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class SmartRestaurantManager extends SmartRestaurantManager_Base {

    private static final int TIMEOUT_SESSION_TIME = 1800000;     // half an hour
    private MessageDigest _md = null;

    private SmartRestaurantManager() {
        FenixFramework.getDomainRoot().setSmartRestaurantManager(this);


        addProduct(new Product("Bife da Vazia", "Um soculento bife da Vazia que o irá fazer chorar por mais.", 12.99, this));
        addProduct(new Product("Bacalhau à Lagareiro", "Experimente o que mais de Português " +
                "nós temos, este bacalhau irá dar-lhe a volta à cabeça", 10.99, this));
        addProduct(new Product("Cozido à Portuguesa", "O nome diz tudo!", 20.59, this));
        addProduct(new Product("Francesinha", "Prove um prato típico do Norte do País, carago!", 7.56, this)); //TODO change this
        addProduct(new Product("Sopa de Peixe", "Porque a sopa faz bem a todos!", 3.56, this));
        addProduct(new Product("Leite Creme", "Experimente este belo leite creme queimado na hora!", 1.56, this));


    }


    public static SmartRestaurantManager getInstance(){
        SmartRestaurantManager mngr = FenixFramework.getDomainRoot().getSmartRestaurantManager();
        if (mngr != null) {
            return mngr;
        }
        System.out.println("New Manager");

        mngr = new SmartRestaurantManager();
        return mngr;

    }

    // Package methods to ensure that "they" only access Facade.
    // Who's "they" you may ask, the Illuminati ofc
    void registerNewUser(String username, String password, String firstName, String lastName, int nif)
            throws UserAlreadyExistsException, NoSuchAlgorithmException, UnsupportedEncodingException {
        if(username == null || password == null || firstName == null || lastName == null) throw new IllegalArgumentException();

        System.out.println(username + " is being registered.");
        canICreateAnUser(username);
        String salt = generateSalt();
        String concat = salt + password;
        MessageDigest md = getMessageDigest();
        byte[] hashedPassword = md.digest(concat.getBytes(StandardCharsets.UTF_8));
        super.addUser(new User(username, hashedPassword, firstName, lastName, nif, salt, this));
    }

    MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
        if(_md == null){
            _md = MessageDigest.getInstance("SHA-256");
        }
        return _md;
    }

    void logOut(byte[] sessionID){
        User u = getUserBySessionId(sessionID);
        u.getSession().remove();
        u.setSession(null);
    }

    byte[] login(String username, String password, int tableNo, int OTP) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (username == null || password == null) throw new IllegalArgumentException();
        System.out.println(username + " is logging in...");
        byte[] hashToken = null;
        User user = getUserByUsername(username);
        passwordChecker(user, password);
         otpChecker(OTP);
         hashToken = generateToken();
         Session s = new Session(hashToken, tableNo);
         s.setUser(user);
         user.setSession(s);
         System.out.println(username + " is logged in.");
        return hashToken;
    }

    private void otpChecker(int otp) {
        // to simplify
        if(otp != 123456) throw new AuthenticatorCodeRejectedException();
    }

    void addRequestToOrder(byte[] sessionId, String productName){
        if(sessionId == null || productName == null) throw new IllegalArgumentException();
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
        if(sessionId == null) throw new IllegalArgumentException();

        User u = getUserBySessionId(sessionId);
        List<Product> productList = new ArrayList<>();
        if(u.getOrder() != null) {
            for (Product p : u.getOrder().getProductSet()) {
                productList.add(p);
            }
        }
        return productList;
    }

    void orderProducts(byte[] sessionId, String hashedPassword) throws NoSuchAlgorithmException {
        if(sessionId == null || hashedPassword == null) throw new IllegalArgumentException();
        User u = getUserBySessionId(sessionId);
        //checkSessionTimeoutAndLogoutUser(u);
        if(u.getSession() != null){
            passwordChecker(u, hashedPassword);
            // Para alem do session id, a password esta correta. Logo, devera ser o utilizador
            System.out.println(u.getUsername() + " is ordering his order.");
            Order toRequest = u.getOrder();
            if(toRequest != null && toRequest.getState() == 0) {
                toRequest.setState(1); // set state meaning that the request is at the kitchen
                //_port.addList(toRequest.getId());
                new KitchenProxy().addList(toRequest.getId());
                // send order to kitchen
            }else{
                throw new AlreadyAskedRequestException();
            }
        }
    }

    void setOrderReadyToDeliver(long orderId){
        setOrderToState(orderId, 2);
        System.out.println("Order " + orderId + " is being delivered.");
        new WaiterProxy().requestToDeliverOrder(orderId);
//        SmartRestaurantHandler.handlerConstants.RCPT_SERVICE_NAME = "KitchenServer"; // hack
    }

    void setOrderToDelivered(long orderId){
        setOrderToState(orderId, 3);
        System.out.println("Order " + orderId + " is delivered.");
//        SmartRestaurantHandler.handlerConstants.RCPT_SERVICE_NAME = "Waiter"; // hack

    }

    void confirmPayment(byte[] sessionId, String hashedPassword, String paypalReference) throws NoSuchAlgorithmException {
        if(sessionId == null || hashedPassword == null || paypalReference == null) throw new IllegalArgumentException();
        User u = getUserBySessionId(sessionId);
        checkSessionTimeoutAndLogoutUser(u);
        if(u.getSession() != null){
            passwordChecker(u, hashedPassword);
            checkPaypalPayment(paypalReference);
            setOrderToPayed(u.getOrder());

        }
    }

    double getPaymentDetails(byte[] sessionId) {
        if(sessionId == null) throw new IllegalArgumentException();
        // Payment details will only be the amount own, just to do something with product's prices
        User u = getUserBySessionId(sessionId);
        checkSessionTimeoutAndLogoutUser(u);
        if(u.getSession() != null){
            if(u.getOrder() != null)
                return u.getOrder().amountToPay();
            else
                throw new OrderDoesntExistException("Order already payed or not ordered at all.");
        }else
            throw new SessionExpiredException(u.getUsername());
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

    private void passwordChecker(User user, String password) throws NoSuchAlgorithmException {
        String salt = user.getSalt();
        String concat = salt + password;
        MessageDigest md = getMessageDigest();
        byte[] hashedPassword = md.digest(concat.getBytes(StandardCharsets.UTF_8));
        System.out.println("Checking password.");
        user.checkLoginTimeout();
        if(!Arrays.equals(hashedPassword, user.getPassword())){
            user.incrementFailedLoginAttempts();
            throw new AccessDeniedException();
        }
    }

    private User getUserByUsername(String username){
        for(User u : getUserSet()){
            if(u.getUsername().equals(username)) return u;
        }
        throw new UserDoesntExistException();
    }

    private User getUserBySessionId(byte[] sessionId){
        for(User u : getUserSet()){
            //checkSessionTimeoutAndLogoutUser(u);
            if(u.getSession() != null && Arrays.equals(u.getSession().getSessionId(), sessionId)){
                checkSessionTimeoutAndLogoutUser(u);
                return u;
            }
        }
        throw new UserWithSessionIdDoenstExistException();
    }

    private Product getProductByName(String name){
        for(Product p : getProductSet()){
            if(p.getName().toLowerCase().equals(name.toLowerCase())) return p; // Lower case is nice
        }
        return null;
    }

    private String generateSalt() throws NoSuchAlgorithmException {
        boolean notFound = true;
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        final byte array[] = new byte[32]; //256bits
        String salt = "";

        while(notFound){
            random.nextBytes(array);
            salt = printBase64Binary(array);
            notFound = false;
            for(User u: getUserSet()) {
                if(u.getSalt() != null && u.getSalt().equals(salt)){
                    notFound=true;
                }
            }
        }
        return salt;
    }

    private byte[] generateToken() throws NoSuchAlgorithmException {
        boolean notFound = true;
        long token;
        byte[] tokenHash = null;
        while(notFound) {
            // Integers can be negative, so to assure unicity for tests we only consider the positive ones
            long randomLong = new BigInteger(128, new Random()).longValue();

            token = randomLong < 0 ? -randomLong : randomLong;
            // Hashing
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            tokenHash = digest.digest(Long.toString(token).getBytes(StandardCharsets.UTF_8));
            notFound = false;
            for(User u : getUserSet()) {
                if(u.getSession() != null) {
                    //checkSessionTimeoutAndLogoutUser(u);
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
            long randomLong = new BigInteger(128, new Random()).longValue();
            id = randomLong < 0 ? -randomLong : randomLong;
            notFound = false;
            for(User u : getUserSet()) {
                if(u.getOrder() != null) {
                    //checkSessionTimeoutAndLogoutUser(u);
                    if (u.getOrder().getId() == id && !notFound)
                        notFound = true;
                }
            }
        }
        return id;
    }

    private void checkSessionTimeoutAndLogoutUser(User u) {
        if(u.getSession() != null){
            if( new DateTime().getMillis() - u.getSession().getLoginTime().getMillis() > TIMEOUT_SESSION_TIME) {
                u.getSession().remove();
                u.setSession(null);
                throw new SessionExpiredException(u.getUsername());
            }
        }
    }

    private void setOrderToState(long orderId, int state) {
        for (User u : getUserSet()) {
            if (u.getOrder() != null && u.getOrder().getId() == orderId) {
                u.getOrder().setState(state);
                return;
            } else
                throw new OrderDoesntExistException();
        }
    }

    private void setOrderToPayed(Order order){
        for (User u : getUserSet()) {
            System.out.println("" + u.getOrder().getId() + " vs " + order.getId());
            if (u.getOrder() != null && u.getOrder().getId() == order.getId()) {
                // Only delivered orders can be payed
                u.getOrder().remove();
                u.setOrder(null);
                System.out.println("Payment was done with success.");
                return;
            }
        }
        throw new OrderDoesntExistException();
    }

    private void checkPaypalPayment(String paypalReference){
        // To simplify, and since this is an university project, we will not contact PayPal
        // We assume that the payment is always completed with success.
        System.out.println("Checking payment reference.");
        try {
            Random random = new Random();
            Thread.sleep(random.nextInt(3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
