import java.sql.*;
import java.util.*;
import java.io.*;
import org.apache.log4j.*;
import java.net.*;
import java.security.*;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500PrivateCredential;

/**
 * A sample program that demonstrates how:
 * to view Data from the ADP web service
 */
public class ImportADP {            

    public static void main(String[] args) {

    long startTime = System.currentTimeMillis();

    ImportADP importAdp = new ImportADP();
    importAdp.initLog4J();

    try {
        System.out.println("Start");

        KeyStore ks = KeyStore.getInstance("JKS");
        java.io.FileInputStream fis = null;
        CertificateFactory cf = null;
        Certificate cert = null;
        try {
            fis = new java.io.FileInputStream("cc.jks");
            ks.load(fis, "password".toCharArray());

            System.out.println("jks loaded.. woohoo");

            FileInputStream certIs = new FileInputStream("C:/Users/david.bekkerus/Desktop/y/cc.crt");
            BufferedInputStream bis = new BufferedInputStream(certIs);

            cf = CertificateFactory.getInstance("X.509");
            cert = cf.generateCertificate(bis);

            System.out.println("cert made");
            System.out.println("cert set");

        } finally {
            if (fis != null) {
                fis.close();
                //certIs.close();
            }
        }

        final char[] KEY_PASSWORD = "password".toCharArray();

        final KeyManagerFactory kmf = 
            KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());

        kmf.init(ks, KEY_PASSWORD);          

        TrustManagerFactory tmf = 
            TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(
            kmf.getKeyManagers(), 
            tmf.getTrustManagers(), 
            new java.security.SecureRandom());  

        SSLSocketFactory sslFactory = ctx.getSocketFactory();
        HttpsURLConnection.setDefaultSSLSocketFactory(sslFactory);

        URL url = new URL(tokenServerUrl);
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();            
        System.out.println("connection: " + conn.toString());


        System.out.println("perm:"+ conn.getPermission());

        //System.out.println("httpGet:"+ httpPost(tokenServerUrl, clientID.toCharArray(), clientSecret.toCharArray()));

        /** this is the problem... always pukes here */
        conn.getResponseMessage();

        conn.disconnect();

    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch(IOException ioe){
        ioe.printStackTrace();
    } catch(Exception e){
         e.printStackTrace();
    } 

    logger.info("End time: " + new java.util.Date() );

}
