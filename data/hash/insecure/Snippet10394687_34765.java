package test;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.onvif.ver10.device.wsdl.Device;
import org.onvif.ver10.schema.DateTime;
import org.onvif.ver10.schema.SystemDateTime;
import org.onvif.ver10.schema.Time;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class OnvifTest {

    private static TimeZone utc = TimeZone.getTimeZone("UTC");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    static {
        sdf.setTimeZone(utc);
    }

    private static long serverTime = 0;
    private static long clientTime = 0;


    private static final String ip = "...";
    private static final String user = "...";
    private static final String pass = "...";
    // Some cameras (e.g. Axis) require that you set the user/pass on the ONVIF section in it's web interface
    // If the camera is reset to factory defaults and was never accessed from the web, then
    // either no user/pass is needed or the default user/pass can be used

    @SuppressWarnings("rawtypes")
    public static void main(String[] args) throws IOException {

        // The altered wsdl file
        URL url = new URL("file://"+System.getProperty("user.home")+"/onvif/devicemgmt.wsdl");
        // This file was downloaded from the onvif website and added a mock service in order to make it complete:
        //  <wsdl:service name="DeviceService">  
        //      <wsdl:port name="DevicePort" binding="tds:DeviceBinding">  
        //          <soap:address location="http://localhost/onvif/device_service"/>  
        //      </wsdl:port>  
        //  </wsdl:service>
        // The altered file was then used to generate java classes using $JAVA_HOME/bin/wsimport -Xnocompile -extension devicemgmt.wsdl 

        QName qname = new QName("http://www.onvif.org/ver10/device/wsdl", "DeviceService");
        Service service = Service.create(url, qname);
        Device device = service.getPort(Device.class);

        BindingProvider bindingProvider = (BindingProvider)device;

        // Add a security handler for the credentials
        final Binding binding = bindingProvider.getBinding();
        List<Handler> handlerList = binding.getHandlerChain();
        if (handlerList == null)
            handlerList = new ArrayList<Handler>();

        handlerList.add(new SecurityHandler());
        binding.setHandlerChain(handlerList);

        // Set the actual web services address instead of the mock service
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://"+ip+"/onvif/device_service"); 

        // Read the time from the server
        SystemDateTime systemDateAndTime = device.getSystemDateAndTime();
        // Mark the local time (no need for an actual clock, the monotone counter will do just fine)
        clientTime = System.nanoTime()/1000000;

        // Generate the server time in msec since epoch
        DateTime utcDateTime = systemDateAndTime.getUTCDateTime();
        org.onvif.ver10.schema.Date date = utcDateTime.getDate();
        Time time = utcDateTime.getTime();
        Calendar c = new GregorianCalendar(utc);
        c.set(date.getYear(), date.getMonth()-1, date.getDay(), time.getHour(), time.getMinute(), time.getSecond());
        System.out.println(sdf.format(c.getTime()));
        serverTime = c.getTimeInMillis();

        // Now try and read something interesting
        Holder<String> manufacturer = new Holder<String>();
        Holder<String> model = new Holder<String>();
        Holder<String> firmwareVersion = new Holder<String>();
        Holder<String> serialNumber = new Holder<String>();
        Holder<String> hardwareId = new Holder<String>();
        device.getDeviceInformation(manufacturer, model, firmwareVersion, serialNumber, hardwareId);
        System.out.println(manufacturer.value);
        System.out.println(model.value);
        System.out.println(firmwareVersion.value);
        System.out.println(serialNumber.value);
        System.out.println(hardwareId.value);
    }

    // Calcualte the password digest from a concatenation of the nonce, the creation time and the password itself
    private static String calculatePasswordDigest(byte[] nonceBytes, String created, String password) {
        String encoded = null;
        try {
            MessageDigest md = MessageDigest.getInstance( "SHA1" );
            md.reset();
            md.update( nonceBytes );
            md.update( created.getBytes() );
            md.update( password.getBytes() );
            byte[] encodedPassword = md.digest();
            encoded = Base64.encode(encodedPassword);
        } catch (NoSuchAlgorithmException ex) {
        }

        return encoded;
    }

    // Calculate what time is it right now on the server
    private static String localToGmtTimestamp() {
        return sdf.format(new Date(System.nanoTime()/1000000 - clientTime + serverTime));
    }

    // This handler will add the authentication parameters
    private static final class SecurityHandler implements SOAPHandler<SOAPMessageContext> {

        @Override
        public boolean handleMessage(final SOAPMessageContext msgCtx) {

            // Indicator telling us which direction this message is going in
            final Boolean outInd = (Boolean) msgCtx.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

            // Handler must only add security headers to outbound messages
            if (outInd.booleanValue() && clientTime!=0 && user!=null && pass!=null) {
                try {
                    // Create the timestamp
                    String timestamp = localToGmtTimestamp();

                    // Generate a random nonce
                    byte[] nonceBytes = new byte[16]; 
                    for (int i=0 ; i<16 ; ++i)
                        nonceBytes[i] = (byte)(Math.random()*256-128);

                    // Digest
                    String dig=calculatePasswordDigest(nonceBytes, timestamp, pass);

                    // Create the xml
                    SOAPEnvelope envelope = msgCtx.getMessage().getSOAPPart().getEnvelope();
                    SOAPHeader header = envelope.getHeader();
                    if (header == null)
                        header = envelope.addHeader();

                    SOAPElement security =
                    header.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");

                    SOAPElement usernameToken =
                    security.addChildElement("UsernameToken", "wsse");

                    SOAPElement username =
                    usernameToken.addChildElement("Username", "wsse");
                    username.addTextNode(user);

                    SOAPElement password =
                    usernameToken.addChildElement("Password", "wsse");
                    password.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest");
                    password.addTextNode(dig);

                    SOAPElement nonce =
                    usernameToken.addChildElement("Nonce", "wsse");
                    nonce.setAttribute("EncodingType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
                    nonce.addTextNode(Base64.encode(nonceBytes));

                    SOAPElement created = usernameToken.addChildElement("Created", "wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
                    created.addTextNode(timestamp);

                } catch (final Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }

        // Other required methods on interface need no guts

        @Override
        public boolean handleFault(SOAPMessageContext context) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void close(MessageContext context) {
            // TODO Auto-generated method stub

        }

        @Override
        public Set<QName> getHeaders() {
            // TODO Auto-generated method stub
            return null;
        }
    }

}
