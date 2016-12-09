package pt.tecnico.ulisboa.smartrestaurant.handler;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Iterator;
import java.util.Set;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

/**
 * Created by tiago on 12/9/16.
 */
public class ChangeHandler implements SOAPHandler<SOAPMessageContext> {
    public static HandlerConstants handlerConstants = new HandlerConstants();

    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        try {
                change(smc);
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        return true;
    }

    public void close(MessageContext messageContext) {
    }

    private void change(SOAPMessageContext smc) throws SOAPException {
        Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        System.out.println("=================CHANGE HANDLER=================");
        if (outbound) {
            System.out.println("Not Changing Outbound SOAP message:");

        } else {
            System.out.println("Changing Outbound SOAP message:");
            changeHeader(smc);
        }
        System.out.println("=================CHANGE HANDLER=================");
    }

    private void changeHeader(SOAPMessageContext smc) throws SOAPException {
        System.out.println("changeHeader()");

        System.out.println(getHandlerConstants().TIMESTAMP);
        SOAPEnvelope envelope = smc.getMessage().getSOAPPart().getEnvelope();
        SOAPHeader header = smc.getMessage().getSOAPPart().getEnvelope().getHeader();
        String timestamp = getHandlerConstants().TIMESTAMP;
        byte[] sig = parseBase64Binary(getHeaderAttribute(envelope, header, timestamp));
        String s = new String(sig);
        System.out.println(s);
    }

    private String getHeaderAttribute(SOAPEnvelope se, SOAPHeader sh, String tag) throws SOAPException {
        System.out.println("Getting attribute " +tag);
        Name name = se.createName(tag,
                getHandlerConstants().PREFIX, getHandlerConstants().NAMESPACE);

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

    private void failMissedFormedSOAP(String info){
        throw new MissedFormedSOAPException(info);
    }

    private void checkSOAPElement(Iterator it) {
        if (!it.hasNext()) {
            failMissedFormedSOAP("Header element not found.");
        }
    }

    public HandlerConstants getHandlerConstants() {
        return handlerConstants;
    }
}
