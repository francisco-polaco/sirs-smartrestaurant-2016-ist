package pt.tecnico.ulisboa.smartrestaurant.handler;

import org.w3c.dom.*;
import pt.tecnico.ulisboa.smartrestaurant.ca.ws.cli.CAClient;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.xml.bind.DatatypeConverter.*;

/**
 * Created by xxlxpto on 07-05-2016.
 */

public class SmartRestarantHandler implements SOAPHandler<SOAPMessageContext> {


    private static final int MAX_MESSAGES_WITHOUT_GETTING_CERTIFICATE_AGAIN = 10;
    private static final String CA_ENDPOINT_ADDRESS = "http://localhost:7070/ca-ws/endpoint";
    public static final String IV = "iv";
    public static HandlerConstants handlerConstants = new HandlerConstants();

    private ArrayList<String> oldTimestamps = new ArrayList<>();
    private AtomicInteger numberMessagesReceive = new AtomicInteger(0);


    public Set<QName> getHeaders() {
        return null;
    }

    public synchronized boolean handleMessage(SOAPMessageContext smc) {
        Boolean outbound = (Boolean) smc
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        try {

           if (outbound) {
               System.out.println("Outbound SOAP message.");
               byte[] iv = getIV();
               formHeader(smc.getMessage());
               signMessage(smc);
               setGetIVFromHeader(smc, iv, true);
               cipherAndDecipherBodyHeader(smc, iv, true);
           } else {
               System.out.print("Inbound SOAP message from: ");
               byte[] iv = setGetIVFromHeader(smc, null, false);
               cipherAndDecipherBodyHeader(smc, iv, false);

               if(handlerConstants.SENDER_SERVICE_NAME.equals(getFieldrFromHeader(smc,handlerConstants.RECEIVER_ELEMENT_NAME)))
                   System.out.println("Receiver matches the receiver");
               else{
                   System.out.println("Receiver does not match the receiver");
                   throw new RuntimeException();
               }

               handlerConstants.RCPT_SERVICE_NAME = getFieldrFromHeader(smc, handlerConstants.SENDER_ELEMENT_NAME);

               if(!checkIfOtherCertificateIsPresent(handlerConstants.RCPT_SERVICE_NAME)){
                   getCertificateFromCA(handlerConstants.RCPT_SERVICE_NAME,
                           handlerConstants.RCPT_SERVICE_NAME + handlerConstants.CERTIFICATE_EXTENSION);
                   numberMessagesReceive.set(0);
               }
               verifySignature(smc, iv);
               verifyTimestampAndNonce(
                       getHeaderAttribute(smc.getMessage().getSOAPPart().getEnvelope(), smc.getMessage().getSOAPPart().getEnvelope().getHeader(), handlerConstants.TIMESTAMP),
                       getHeaderAttribute(smc.getMessage().getSOAPPart().getEnvelope(), smc.getMessage().getSOAPPart().getEnvelope().getHeader(), handlerConstants.NONCE)
                       );
               removeHeader(smc.getMessage());
        }

        }catch(AuthenticationException | MissedFormedSOAPException | javax.xml.ws.WebServiceException | InvalidTimestampSOAPException e){
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println("Caught exception in handleMessage: " + e.getMessage());
            e.printStackTrace();
            if(isAValidSenderName())
                return false;
            else{
                throw new RuntimeException();
            }
        }
        return true;
    }

    private boolean isAValidSenderName() {
        return handlerConstants.SENDER_SERVICE_NAME.equals("OrderServer") || handlerConstants.SENDER_SERVICE_NAME.equals("Waiter") || handlerConstants.SENDER_SERVICE_NAME.equals("KitchenServer");
    }

    private void formHeader(SOAPMessage message) throws Exception {
        // get the soap parts
        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope se = sp.getEnvelope();
        SOAPHeader sh = se.getHeader();
        SOAPBody sb = se.getBody();

        sh.removeContents();

        // add the sender
        insertElement(se, sh, handlerConstants.SENDER_ELEMENT_NAME, "Adding sender to SOAP...", handlerConstants.SENDER_SERVICE_NAME);

        // add the receiver
        insertElement(se, sh, handlerConstants.RECEIVER_ELEMENT_NAME, "Adding receiver to SOAP...", handlerConstants.RCPT_SERVICE_NAME);

        // add timestamp
        insertElement(se, sh, handlerConstants.TIMESTAMP, "Adding Timestamp to SOAP...", actualTime().toString());

        // add nounce
        insertElement(se, sh, handlerConstants.NONCE, "Adding Nonce to SOAP...", secureRandomNonce());

        message.saveChanges();
    }

    private void signMessage(SOAPMessageContext smc) throws Exception {

        System.out.println("Signing... ");

        SOAPMessage msg = smc.getMessage();
        SOAPPart sp = msg.getSOAPPart();
        SOAPEnvelope se = sp.getEnvelope();
        SOAPHeader sh = se.getHeader();

        byte[] plainBytes = getSOAPtoByteArray(smc);
        byte[] digitalSignature = makeDigitalSignature(plainBytes,
                getPrivateKeyFromKeystore(handlerConstants.SENDER_SERVICE_NAME + handlerConstants.KEYSTORE_EXTENSION,
                        handlerConstants.KEYSTORE_PASSWORD.toCharArray(),
                        handlerConstants.SENDER_SERVICE_NAME, handlerConstants.KEY_PASSWORD.toCharArray()));

        checkOwnSignature(smc, digitalSignature);

        insertElement(se, sh, handlerConstants.SIG_ELEMENT_NAME, "Adding signature to SOAP...", printBase64Binary(digitalSignature));
        msg.saveChanges();
    }

    // Mode = true (cipher) , Mode = false (decipher)
    private void cipherAndDecipherBodyHeader(SOAPMessageContext smc, byte[] iv, boolean mode) throws Exception {

        SOAPMessage msg = smc.getMessage();
        SOAPPart sp = msg.getSOAPPart();
        SOAPEnvelope se = sp.getEnvelope();
        SOAPBody sb = se.getBody();
        SOAPHeader sh = se.getHeader();

        // cipher/decipher the header and convert to base64
        SOAPHeaderElement she;
        for(Iterator<SOAPHeaderElement> iterH = sh.getChildElements(); iterH.hasNext(); ) {
            she = iterH.next();
            if (mode) {
                // skip the IV and Signature
                if(she.getTagName().equals(handlerConstants.PREFIX + ":" + IV) || she.getTagName().equals(handlerConstants.PREFIX + ":" + handlerConstants.SIG_ELEMENT_NAME))
                    continue;
                she.setTextContent(cipherContent(she.getTextContent(), iv));
            }
            else {
                // skip the IV and Signature
                if(she.getTagName().equals(handlerConstants.PREFIX + ":" + IV) || she.getTagName().equals(handlerConstants.PREFIX + ":" + handlerConstants.SIG_ELEMENT_NAME))
                    continue;
                she.setTextContent(new String(decipherMessage(parseBase64Binary(she.getTextContent()), iv)));
            }

        }

        if(mode)
            System.out.println("Mounting the body");
        else
            System.out.println("Unmouting the body");

        // cipher/decipher the body and convert to base64
        SOAPBodyElement sbe;
        ArrayList<org.w3c.dom.Node> textNodes = getBodyMap(sb);
        for(org.w3c.dom.Node n : textNodes) {
            if(n.getTextContent().equals(""))
                continue;
            if(mode)
                n.setTextContent(cipherContent(n.getTextContent(), iv));
            else
                n.setTextContent(new String(decipherMessage(parseBase64Binary(n.getTextContent()), iv)));
        }
        msg.saveChanges();
    }

    private ArrayList<org.w3c.dom.Node> getBodyMap(SOAPBody sb) throws SOAPException {
        NodeList nl = sb.getChildNodes();
        ArrayList<org.w3c.dom.Node> output = new ArrayList<>();
        for(int i = 0; i<nl.getLength(); i++){
            recursive(nl.item(i), output);
        }
        return output;
    }

    private void recursive(org.w3c.dom.Node n, ArrayList<org.w3c.dom.Node> output){
        if(n.getFirstChild() == null)
            output.add(n);
        else{
            NodeList nl = n.getChildNodes();
            for(int i = 0; i<nl.getLength(); i++){
                recursive(nl.item(i),output);
            }
        }
    }

    private byte[] setGetIVFromHeader(SOAPMessageContext smc, byte[] iv, boolean mode) throws SOAPException {
        SOAPPart sp = smc.getMessage().getSOAPPart();
        SOAPEnvelope se = sp.getEnvelope();
        SOAPHeader sh = se.getHeader();

        if(mode){
            insertElement(se, sh, IV, "Adding IV to SOAP...", printBase64Binary(iv));
            smc.getMessage().saveChanges();
            return null;
        }

        System.out.println("Removing IV from SOAP...");
        String content = getHeaderAttribute(se, sh, IV);
        smc.getMessage().saveChanges();

        return parseBase64Binary(content);
    }

    private String getFieldrFromHeader(SOAPMessageContext smc, String tag) throws Exception {

        // get SOAP envelope header
        SOAPMessage msg = smc.getMessage();
        SOAPPart sp = msg.getSOAPPart();
        SOAPEnvelope se = sp.getEnvelope();
        SOAPHeader sh = se.getHeader();

        Name name = se.createName(tag, handlerConstants.PREFIX, handlerConstants.NAMESPACE);
        Iterator it = sh.getChildElements(name);
        // check header element
        checkSOAPElement(it);

        SOAPElement element = (SOAPElement) it.next();
        String elementValue = element.getTextContent();

        return elementValue;
    }

    private void removeHeader(SOAPMessage msg) throws SOAPException {
        SOAPPart sp = msg.getSOAPPart();
        SOAPEnvelope se = sp.getEnvelope();
        SOAPHeader sh = se.getHeader();

        sh.removeContents();
        msg.saveChanges();
    }

    private void verifySignature(SOAPMessageContext smc, byte[] iv) throws Exception {
        System.out.println("Verifying Signature... ");
        byte[] signature = getSignatureFromSoap(smc, iv);
        smc.getMessage().saveChanges();
        Certificate certificate = readCertificateFile(handlerConstants.RCPT_SERVICE_NAME + handlerConstants.CERTIFICATE_EXTENSION);
        if(certificate == null){
            failAuthentication("Could not open the Recipient's certificate.");
        }
        PublicKey publicKey = certificate.getPublicKey();
        boolean isValid = verifyDigitalSignature(signature, getSOAPtoByteArray(smc), publicKey);
        if (isValid) {
            System.out.println("The digital signature is valid");
        } else {
            System.out.println("The digital signature is NOT valid");
            failAuthentication("Recipient's authentication is not valid.");
        }
    }

    private void failAuthentication(String info) throws AuthenticationException {
        throw new AuthenticationException(info);
    }

    private void checkOwnSignature(SOAPMessageContext smc, byte[] signature)
            throws Exception {
        System.out.println("Checking signature...");
        KeyStore keystore = readKeystoreFile(handlerConstants.SENDER_SERVICE_NAME + handlerConstants.KEYSTORE_EXTENSION,
                handlerConstants.KEYSTORE_PASSWORD.toCharArray());
        if(keystore == null){
            failAuthentication("KeyStore doesn't exist.");
        }
        Certificate certificate = keystore.getCertificate(handlerConstants.SENDER_SERVICE_NAME);
        PublicKey publicKey = certificate.getPublicKey();
        boolean isValid = verifyDigitalSignature(signature, getSOAPtoByteArray(smc), publicKey);
        if (isValid) {
            System.out.println("The digital signature is valid");
        } else {
            System.out.println("The digital signature is NOT valid");
            failAuthentication("Own signature is not valid.");
        }
    }

    private void getCertificateFromCA(String entity, String filename) throws Exception {
        CAClient caClient = new CAClient(CA_ENDPOINT_ADDRESS);
        try{
            caClient.getAndWriteEntityCertificate(entity, filename);
        }catch (IOException e){
            failAuthentication("Error downloading certificate.");
        }
        Certificate certificate = readCertificateFile(filename);
        KeyStore keyStore = readKeystoreFile(handlerConstants.SENDER_SERVICE_NAME + ".jks",
                handlerConstants.KEYSTORE_PASSWORD.toCharArray());
        if(keyStore == null){
            failAuthentication("KeyStore doesn't exist.");
        }
        Certificate caCertificate =  keyStore.getCertificate("ca");
        PublicKey caPublicKey = caCertificate.getPublicKey();
        System.out.println("Checking Certificate from CA...");
        if (verifySignedCertificate(certificate, caPublicKey)) {
            System.out.println("The signed certificate is valid");
        } else {
            System.err.println("The signed certificate is not valid");
            failAuthentication("Sender's certificate is not valid.");
        }
    }

    private byte[] getSOAPtoByteArray(SOAPMessageContext smc) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            smc.getMessage().writeTo(out);
        } catch (SOAPException | IOException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    private void insertElement(SOAPEnvelope se, SOAPHeader sh, String tag, String print, String text) throws SOAPException {
        // add header element (name, namespace prefix, namespace)
        Name name = se.createName(tag,
                handlerConstants.PREFIX, handlerConstants.NAMESPACE);
        SOAPHeaderElement element = sh.addHeaderElement(name);
        System.out.println(print);
        // add header element value

        element.addTextNode(text);
    }

    private String secureRandomNonce() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        final byte array[] = new byte[16]; //128bits
        random.nextBytes(array);
        return printBase64Binary(array);
    }

    private Timestamp actualTime(){
        GregorianCalendar rightNow = new GregorianCalendar();
        return new Timestamp(rightNow.getTimeInMillis());
    }

    private void verifyTimestampAndNonce(String date, String nonce) {
        Timestamp stamp = actualTime();


        if (stamp.before(Timestamp.valueOf(date))) {
            throw new InvalidTimestampSOAPException("Out of range");
        }

        if (stamp.getMinutes() >= 1) {
            stamp.setMinutes(stamp.getMinutes() - 1);
        }else
            stamp.setSeconds(0);

        if (stamp.after(Timestamp.valueOf(date))) {
            throw new InvalidTimestampSOAPException("Out of range");
        }

        if (oldTimestamps.size() != 0) {
            if (oldTimestamps.contains(date + nonce)) {
                throw new InvalidTimestampSOAPException("Timestamp + nonce already used");
            }
        }
        oldTimestamps.add(date + nonce);

    }

    private void checkSOAPElement(Iterator it) {
        if (!it.hasNext()) {
            failMissedFormedSOAP("Header element not found.");
        }
    }

    private void checkSOAPHeader(SOAPHeader sh) {
        if (sh == null) {
            failMissedFormedSOAP("Header not found.");
        }
    }

    private byte[] getSignatureFromSoap(SOAPMessageContext smc, byte[] iv) throws SOAPException, NoSuchPaddingException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException, IOException {

        // get SOAP envelope header
        SOAPMessage msg = smc.getMessage();
        SOAPPart sp = msg.getSOAPPart();
        SOAPEnvelope se = sp.getEnvelope();
        SOAPHeader sh = se.getHeader();

        // check header
        checkSOAPHeader(sh);

        byte[] sig = parseBase64Binary(getHeaderAttribute(se, sh, handlerConstants.SIG_ELEMENT_NAME));
        String signature = new String(sig);

        // put header in a property context
        smc.put(handlerConstants.CONTEXT_PROPERTY, signature);
        // set property scope to application client/server class can access it
        smc.setScope(handlerConstants.CONTEXT_PROPERTY, MessageContext.Scope.APPLICATION);

        return sig;
    }

    // Cipher

    private String cipherContent(String input, byte[] iv) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        // get a AES private key
        Key key = getAESKey();

        // get a AES cipher object and print the provider
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");


        System.out.println("Ciphering ...");

        IvParameterSpec ips = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ips);

        byte[] cipheredInput = cipher.doFinal(input.getBytes());

        return printBase64Binary(cipheredInput);
    }

    private byte[] getIV() throws NoSuchAlgorithmException {
        // generate sample AES 16 byte initialization vector
        byte[] iv = new byte[16]; // 128 bits
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.nextBytes(iv);
        return iv;
    }

    private byte[] decipherMessage(byte[] input, byte[] iv)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, IOException, SOAPException {

        Key key = getAESKey();

        // get a AES cipher object and print the provider
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //System.out.println(cipher.getProvider().getInfo());

        System.out.println("Deciphering ...");

        IvParameterSpec ips = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ips);
        return cipher.doFinal(input);
    }

    private String getHeaderAttribute(SOAPEnvelope se, SOAPHeader sh, String tag) throws SOAPException {
        Name name = se.createName(tag,
                handlerConstants.PREFIX, handlerConstants.NAMESPACE);

        Iterator it = sh.getChildElements(name);
        // check header element
        checkSOAPElement(it);

        SOAPElement element = (SOAPElement) it.next();
        String elementValue = element.getTextContent();

        it.remove();
        element.removeAttribute(name);
        element.removeContents();
        return elementValue;
    }

    private Key getAESKey() throws IOException {
        // get a AES private key
        /*System.out.println("Generating AES key ...");
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        Key key = keyGen.generateKey();
        System.out.println("Key:");
        System.out.println(printHexBinary(key.getEncoded()));
        return key;*/
        return readAESKey(handlerConstants.AES_KEY_FILE);
    }

    private Key readAESKey(String keyPath) throws IOException {
        //System.out.println("Reading key from file " + keyPath + " ...");
        FileInputStream fis = new FileInputStream(keyPath);
        byte[] encoded = new byte[fis.available()];
        fis.read(encoded);
        fis.close();
        /*System.out.println("Key:");
        System.out.println(printHexBinary(encoded));*/

        SecretKeySpec keySpec = new SecretKeySpec(encoded, "AES");
        // keySpec.
        // SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("AES");
        // Key key = keyFactory.generateSecret(keySpec);

        return keySpec;
    }

    public boolean handleFault(SOAPMessageContext smc) {

        return true;
    }

    // nothing to clean up
    public void close(MessageContext messageContext) {
    }

    private synchronized boolean checkIfOtherCertificateIsPresent(String entity){
        if(!(new File(entity + handlerConstants.CERTIFICATE_EXTENSION)).exists() ||
                numberMessagesReceive.get() >= MAX_MESSAGES_WITHOUT_GETTING_CERTIFICATE_AGAIN){
            System.out.printf("We need to refresh the %s certificate.\n", entity);
            return false;
        } else {
            System.out.printf("%s certificate is present. Times until renewal: %d\n", entity,
                    MAX_MESSAGES_WITHOUT_GETTING_CERTIFICATE_AGAIN - numberMessagesReceive.getAndIncrement());
            return true;
        }
    }

    private void failMissedFormedSOAP(String info){
        throw new MissedFormedSOAPException(info);
    }

    /**
     * Reads a certificate from a file
     *
     * @return Certificate
     * @throws Exception
     */
    private Certificate readCertificateFile(String certificateFilePath) throws Exception {
        FileInputStream fis;

        try {
            fis = new FileInputStream(certificateFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("Certificate file <" + certificateFilePath + "> not found.");
            return null;
        }
        BufferedInputStream bis = new BufferedInputStream(fis);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        if (bis.available() > 0) {
            return cf.generateCertificate(bis);
            // It is possible to print the content of the certificate file:
            // System.out.println(cert.toString());
        }
        bis.close();
        fis.close();
        return null;
    }

    /**
     * Verifica se um certificado foi devidamente assinado pela CA
     *
     * @param certificate
     *            certificado a ser verificado
     * @param caPublicKey
     *            certificado da CA
     * @return true se foi devidamente assinado
     */
    private boolean verifySignedCertificate(Certificate certificate, PublicKey caPublicKey) {
        try {
            certificate.verify(caPublicKey);
        } catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
                | SignatureException e) {
            System.err.println("ERRO VERIFICACAO CERTIFICADO:\n" + e.getMessage());
            // O método Certifecate.verify() não retorna qualquer valor (void).
            // Quando um certificado é inválido, isto é, não foi devidamente
            // assinado pela CA
            // é lançada uma excepção: java.security.SignatureException:
            // Signature does not match.
            // também são lançadas excepções caso o certificado esteja num
            // formato incorrecto ou tenha uma
            // chave inválida.

            return false;
        }
        return true;
    }

    /**
     * Reads a PrivateKey from a key-store
     *
     * @return The PrivateKey
     * @throws Exception
     */
    private PrivateKey getPrivateKeyFromKeystore(String keyStoreFilePath, char[] keyStorePassword,
                                                       String keyAlias, char[] keyPassword) throws Exception {

        KeyStore keystore = readKeystoreFile(keyStoreFilePath, keyStorePassword);

        return (PrivateKey) keystore.getKey(keyAlias, keyPassword);
    }

    /**
     * Reads a KeyStore from a file
     *
     * @return The readAESKey KeyStore
     * @throws Exception
     */
    private KeyStore readKeystoreFile(String keyStoreFilePath, char[] keyStorePassword) throws Exception {
        FileInputStream fis;
        try {
            fis = new FileInputStream(keyStoreFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("Keystore file <" + keyStoreFilePath + "> not found.");
            return null;
        }
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(fis, keyStorePassword);
        return keystore;
    }

    /** auxiliary method to calculate digest from text and cipher it */
    private byte[] makeDigitalSignature(byte[] bytes, PrivateKey privateKey) throws Exception {

        // get a signature object using the SHA-1 and RSA combo
        // and sign the plain-text with the private key
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initSign(privateKey);
        sig.update(bytes);

        return sig.sign();
    }

    /**
     * auxiliary method to calculate new digest from text and compare it to the
     * to deciphered digest
     */
    private boolean verifyDigitalSignature(byte[] cipherDigest, byte[] bytes, PublicKey publicKey)
            throws Exception {

        // verify the signature with the public key
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initVerify(publicKey);
        sig.update(bytes);
        try {
            return sig.verify(cipherDigest);
        } catch (SignatureException se) {
            System.err.println("Caught exception while verifying signature " + se);
            return false;
        }
    }
}