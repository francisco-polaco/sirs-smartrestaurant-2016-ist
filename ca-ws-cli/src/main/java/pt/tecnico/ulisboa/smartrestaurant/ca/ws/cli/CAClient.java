package pt.tecnico.ulisboa.smartrestaurant.ca.ws.cli;

import pt.tecnico.ulisboa.smartrestaurant.ca.ws.CA;
import pt.tecnico.ulisboa.smartrestaurant.ca.ws.CAImplementationService;

import javax.xml.ws.BindingProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

/**
 * Created by xxlxpto on 06-05-2016.
 */

public class CAClient {
    private CA mCa;

    public CAClient(String endpointAddress){
        System.out.println("============CA Client============");
        System.out.println("Creating CA Client...");
        CAImplementationService tttImplService = new CAImplementationService();
        mCa = tttImplService.getCAImplementationPort();


        if (endpointAddress == null) {
            System.out.println("Not found!");
            return;
        } else {
            System.out.printf("Found %s%n", endpointAddress);
        }

        System.out.println("Creating stub ...");


        System.out.println("Setting endpoint address ...");
        BindingProvider bindingProvider = (BindingProvider) mCa;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
    }

    public void getAndWriteEntityCertificate(String entity, String filename)
            throws IOException{
        byte[] certificate = mCa.getEntityCertificate(entity);
        failIfDirectoryTraversal(filename);
        File f = new File(filename);
        if(f.createNewFile()) {
            System.out.println("Writing File " + filename);
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            fileOutputStream.write(certificate);
            fileOutputStream.close();
        }
        System.out.println("============END: CA Client============");
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
