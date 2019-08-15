package mail.msexchangetest;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import microsoft.exchange.webservices.data.ClientCertificateCredentials;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.Folder;
import microsoft.exchange.webservices.data.FolderId;
import microsoft.exchange.webservices.data.Mailbox;
import microsoft.exchange.webservices.data.MessageBody;
import microsoft.exchange.webservices.data.ServiceLocalException;
import microsoft.exchange.webservices.data.WebCredentials;
import microsoft.exchange.webservices.data.WellKnownFolderName;

\/**
 * 
 *
 *\/
public class App \{


    private static TrustManagerFactory tmf;
    private static SSLContext ctx ;

    private static TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager(){
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String    authType){
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType){
            }
        }};
        // The trust all certs.

        private static void setSSLConfigManual() throws Exception 
        {

            KeyStore ks=KeyStore.getInstance("pkcs12");
            ks.load(new FileInputStream("/home/user/Documents/private/mail-cert/compUser.pfx"),"mypass".toCharArray());

            System.out.println("init Stores...");

            KeyManagerFactory kmf=KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks,"mypass".toCharArray());

            ctx= SSLContext.getInstance("TLS");

            ctx.init(kmf.getKeyManagers(), trustAllCerts, new SecureRandom());

            SSLContext.setDefault(ctx);

        }



    public static void main( String[] args ) throws URISyntaxException, Exception
    {

        setSSLConfigManual();

        System.out.println("=============BEGIN HANDSHAKE=============");
        testConnect();
        System.out.print(">");
        System.in.read();
        System.out.println("=============END HANDSHAKE=============");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("=============BEGIN EXCHANGE_2007 MESSAGE SEND=============");
        try{
            send2007Message();
        } catch (Exception ex){
            System.out.println("=============ERROR EXCHANGE_2007 MESSAGE SEND=============");
            System.out.print(">");
            System.in.read();
            ex.printStackTrace();
            System.out.print(">");
            System.in.read();
        }
        System.out.println("=============END EXCHANGE_2007 MESSAGE SEND=============");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("=============BEGIN EXCHANGE_2010 MESSAGE SEND=============");
        try{
            send2010Message();            
        } catch (Exception ex){
            System.out.println("=============ERROR EXCHANGE_2010 MESSAGE SEND=============");        
            System.out.print(">");
            System.in.read();
            ex.printStackTrace();
            System.out.print(">");
            System.in.read();
        }
        System.out.println("=============END EXCHANGE_2010 MESSAGE SEND=============");        

    }

    private static void send2010Message() throws ServiceLocalException, Exception, URISyntaxException {
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);


        ExchangeCredentials credentials = new WebCredentials(
                                                "username",
                                                "userpass","DOMAIN");
        service.setCredentials(credentials);

        service.setTraceEnabled(true);        
        service.setUrl(new URI("https://mail.server.country/"));

        service.setTimeout(100*1000);


        Folder myFolder = new Folder(service);
        myFolder.setDisplayName("My EWS Test Folder");
        FolderId rootFolderId = new FolderId(WellKnownFolderName.Root, new Mailbox("user@server.country" ));
        myFolder.save(rootFolderId);

        EmailMessage msg= new EmailMessage(service);
        msg.setSubject("Test message "+System.currentTimeMillis()); 
        msg.setBody(MessageBody.getMessageBodyFromText("Sent using the EWS Managed API."));
        msg.getToRecipients().add("User@gmail.com");

        msg.send();
    }

    private static void send2007Message() throws ServiceLocalException, Exception, URISyntaxException {
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2007_SP1);

        ExchangeCredentials credentials = new WebCredentials(
                                                "user",
                                                "pass","DOMAIN");
        service.setCredentials(credentials);

        service.setTraceEnabled(true);                
        service.setUrl(new URI("https://legacy.server.country"));
        service.setTimeout(100*1000);


        EmailMessage msg= new EmailMessage(service);
        msg.setSubject("Test message "+System.currentTimeMillis()); 
        msg.setBody(MessageBody.getMessageBodyFromText("Sent using the EWS Managed API."));
        msg.getToRecipients().add("User@gmail.com");

        msg.send();
    }

    private static void testConnect() throws IOException {
        SSLSocketFactory factory = ctx.getSocketFactory();
        SSLSocket sslsocket = (SSLSocket) factory.createSocket( 
                "mail.server.country",443);
        sslsocket.setUseClientMode(true);
        sslsocket.setSoTimeout(100000);
        sslsocket.addHandshakeCompletedListener(new MyHandshakeListener());
        sslsocket.startHandshake();           
    }
    public static class MyHandshakeListener implements HandshakeCompletedListener {
        public void handshakeCompleted(HandshakeCompletedEvent e) {
            System.out.println("Handshake succesful!");
            System.out.println("Using cipher suite: " + e.getCipherSuite());
        }
    }

}
