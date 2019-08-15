import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.TimeZone;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.impl.dv.util.Base64;
import org.w3c.dom.Node;


public class SOAPSecurity3 {

    private static String calculatePasswordDigest(String nonce, String created, String password) {
        String encoded = null;
        try {
            String pass = hexEncode(nonce) + created + password;
            MessageDigest md = MessageDigest.getInstance( "SHA1" );
            md.update( pass.getBytes() );
            byte[] encodedPassword = md.digest();
            encoded = Base64.encode(encodedPassword);
        } catch (NoSuchAlgorithmException ex) {
           /* Logger.getLogger(HeaderHandler.class.getName()).log(Level.SEVERE, null, ex);*/
        }

        return encoded;
    }

    private static String hexEncode(String in) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < (in.length() - 2) + 1; i = i + 2) {
            int c = Integer.parseInt(in.substring(i, i + 2), 16);
            char chr = (char) c;
            sb.append(chr);
        }
        return sb.toString();
    }

    private static SOAPMessage createSoapRequest(String value) throws Exception{

        //This is used to get time in SOAP request in yyyy-MM-dd'T'HH:mm:ss.SSS'Z' format
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:dd.SSS'Z'");
      formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        //This is for UsernameToken element
        Random generator = new Random();
        String nonceString = String.valueOf(generator.nextInt(999999999)); // This generate random nonce
        Date timestamp = new java.util.Date();
        String pass = "password";
        String user = "username";
        //This is for UsernameToken element ends


        //This is for TimeStamp element value
        java.util.Date create = new java.util.Date();
        java.util.Date expires = new java.util.Date(create.getTime() + (5l * 60l * 1000l));
        //This is for TimeStamp value ends

         MessageFactory messageFactory = MessageFactory.newInstance();
         SOAPMessage soapMessage = messageFactory.createMessage();
         SOAPPart soapPart = soapMessage.getSOAPPart();
        //SOAP Envelope
         SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
         soapEnvelope.addNamespaceDeclaration("v1", "http://services.test.getDesignation.com/schema/MainData/V1");

    //SOAP Header            
         SOAPHeader header = soapMessage.getSOAPHeader(); 
         if (header == null) {
             header = soapEnvelope.addHeader();
         }

         SOAPElement security =header.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd").addAttribute(new QName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");


                SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse").addAttribute(QName.valueOf("wsu:Id"),"UsernameToken-89293AC6E584F11ADF141358720544137");

                // add the username to usernameToken
                SOAPElement userNameSOAPElement = usernameToken.addChildElement("Username","wsse");
                userNameSOAPElement.addTextNode("username");

                // add the password to usernameToken
                SOAPElement passwordSOAPElement = usernameToken.addChildElement("Password","wsse").addAttribute(new QName("Type"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
                passwordSOAPElement.addTextNode("password");


                //Adding random Nonce
                SOAPElement nonce =usernameToken.addChildElement("Nonce", "wsse").addAttribute(new QName("EncodingType"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
                nonce.addTextNode(Base64.encode(hexEncode(nonceString).getBytes()));

                //Adding created element of UsernameToken
                SOAPElement created = usernameToken.addChildElement("Created", "wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
                created.addTextNode(formatter.format(timestamp)); //formatter formats the date to String

                //Adding Timestamp
                SOAPElement timestampElem = security.addChildElement("Timestamp", "wsu").addAttribute(QName.valueOf("wsu:Id"),"TS-1EB4A2A52467EB9373141362942343119");
                SOAPElement elem = timestampElem.addChildElement("Created", "wsu");
                elem.addTextNode(formatter.format(create)); //formatter formats the date to String
                timestampElem.addChildElement(elem);
                elem = timestampElem.addChildElement("Expires", "wsu");
                elem.addTextNode(formatter.format(expires)); //formatter formats the date to String
                timestampElem.addChildElement(elem);


         //////SOAP Body
         SOAPBody soapBody = soapEnvelope.getBody();
         SOAPElement soapElement = soapBody.addChildElement("getDesignationRequest", "v1");
         SOAPElement element1 = soapElement.addChildElement("DesignationCode", "v1");
         element1.addTextNode(value);
         soapMessage.saveChanges();
         System.out.println("----------SOAP Request------------");
         soapMessage.writeTo(System.out);
         return soapMessage;
     }
     private static void createSoapResponse(SOAPMessage soapResponse) throws Exception  {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.println("\n----------SOAP Response-----------");
        /////////////////////////////////////////////////////
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
        System.out.println();
     }
     public static void main(String args[]) throws Exception{

            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            String url = "http://localhost:8090/designation";
            SOAPMessage soapRequest = createSoapRequest("SSE");


            SOAPMessage soapResponse = soapConnection.call(soapRequest, url);
            createSoapResponse(soapResponse);

            String Code =soapResponse.getSOAPBody().getElementsByTagName("Code").item(0).getFirstChild().getNodeValue();

          if(Code.equals("Success"))
          {
            String Result=soapResponse.getSOAPBody().getElementsByTagName("DesignationCodeResult").item(0).getFirstChild().getNodeValue();
            System.out.println(Result ); 
          }
          else 
          {
              System.out.println("SOAP Fault");
          }



            soapConnection.close();
        }
     }
