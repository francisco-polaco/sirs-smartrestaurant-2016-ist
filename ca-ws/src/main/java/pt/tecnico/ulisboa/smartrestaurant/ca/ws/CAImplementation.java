package pt.tecnico.ulisboa.smartrestaurant.ca.ws;

import pt.tecnico.ulisboa.smartrestaurant.ca.ws.exception.CertificateDoesntExistsException;
import pt.tecnico.ulisboa.smartrestaurant.ca.ws.exception.CertificateIsInBlackListException;

import javax.jws.WebService;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;


/**
 * Created by xxlxpto on 06-05-2016.
 */
@WebService(endpointInterface = "pt.tecnico.ulisboa.smartrestaurant.ca.ws.CA")
public class CAImplementation implements CA {

    private final static String CA_CERTIFICATE_FILE = "certs/ca-certificate.pem.txt";

    private ArrayList<String> fileNames = new ArrayList<>();
    private ArrayList<Certificate> blackList = new ArrayList<>();

    public CAImplementation(){
        File dir = new File("certs/");
        if(!dir.exists()) dir.mkdir();
        for (File file : dir.listFiles()) {
            if (file.getName().startsWith("Waiter") && file.getName().endsWith(".cer") ) {
                fileNames.add(file.getName());
            }
        }
        fileNames.add("KitchenServer.cer");
        fileNames.add("KitchenClient.cer");
        fileNames.add("OrderServer.cer");

        for(String f: fileNames){
            System.out.println(f);
        }
        System.out.println("Number of certificates loaded: " + fileNames.size());
    }

    public void addEntityToBlackList(String entity) throws IOException, CertificateException {
        final String certificatePath = "certs/" + entity + ".cer";
        Certificate certificate = readCertificate(certificatePath);
        System.out.println("Adding " + certificatePath + " to the blacklist...");
        blackList.add(certificate);
        System.out.println(entity + " blacklisted!");
    }

    @Override
    public byte[] getEntityCertificate(String entity) throws CertificateDoesntExistsException {
        if(entity == null){
            return null;
        }
        System.out.println(entity + " Certificate Requested...");
        if(fileNames.contains(entity + ".cer")){
            try {
                return readCertificateFile(entity);
            } catch (IOException | CertificateException e){
                System.out.println("We are having problems with our certificates.\n" + e.getMessage());
                throw new CertificateDoesntExistsException(entity);
            }
        }else{
            throw new CertificateDoesntExistsException(entity);
        }
    }

    /**
     * Reads a certificate from a file
     *
     * @return
     * @throws IOException
     */
    private byte[] readCertificateFile(String entity) throws IOException, CertificateException {
        String certificateFilePath = "certs/" + entity + ".cer";
        Certificate certificate = readCertificate(certificateFilePath);

        if(blackList.contains(certificate)) throw new CertificateIsInBlackListException(entity);

        Certificate caCertificate = readCertificate(CA_CERTIFICATE_FILE);
        PublicKey caPublicKey = caCertificate.getPublicKey();

        if (verifySignedCertificate(certificate, caPublicKey)) {
            System.out.println("The asked certificate is valid");
        } else {
            System.err.println("The asked certificate is not valid");
            throw new CertificateException();
        }
        return Files.readAllBytes(Paths.get(certificateFilePath));

    }

    /**
     * Reads a certificate from a file
     *
     * @return
     * @throws Exception
     */
    private Certificate readCertificate(String certificateFilePath) throws IOException, CertificateException {
        FileInputStream fis;
        failIfDirectoryTraversal(certificateFilePath);
        try {
            fis = new FileInputStream(certificateFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("Certificate file <" + certificateFilePath + "> not found.");
            return null;
        }
        BufferedInputStream bis = new BufferedInputStream(fis);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        if (bis.available() > 0) {
            Certificate cert = cf.generateCertificate(bis);
            return cert;
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

    private void failIfDirectoryTraversal(String relativePath)
    {
        File file = new File(relativePath);

        String pathUsingCanonical;
        String pathUsingAbsolute;
        try
        {
            pathUsingCanonical = file.getCanonicalPath();
            pathUsingAbsolute = file.getAbsolutePath();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Directory traversal attempt?", e);
        }

        if (! pathUsingCanonical.equals(pathUsingAbsolute))
        {
            throw new RuntimeException("Directory traversal attempt!");
        }
    }

}
