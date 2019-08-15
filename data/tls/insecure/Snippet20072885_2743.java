package ssl;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package testssl;

/**
 *
 * @author Alabi
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.KeyStore;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class TwoWaySSL2 {

        private static String keyStoreFile = "epaysadad.jks";
        private static String trustStoreFile ="SADADTrustStore.jks";
        private static String KeyStoreFilePasswd = "pw1111";
        private static String TrustStoreFilePasswd = "pw2222";


    public static void main(String[] args) {

        try{
            System.setProperty("Dhttps.protocols", "SSLv3");
            System.setProperty("Djavax.net.debug", "all");

            KeyStore ks = KeyStore.getInstance("JKS");
            KeyStore ts = KeyStore.getInstance("JKS");

            char[] Keypassphrase = KeyStoreFilePasswd.toCharArray();
            char[] Trustpassphrase = TrustStoreFilePasswd.toCharArray();

            ks.load(new FileInputStream(keyStoreFile), Keypassphrase);
            ts.load(new FileInputStream(trustStoreFile), Trustpassphrase);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, Keypassphrase);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ts);

            SSLContext sslCtx = SSLContext.getInstance("SSLv3");

            sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            HttpsURLConnection.setDefaultSSLSocketFactory(sslCtx.getSocketFactory());

            URL url = new URL("serverURL");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(sslCtx.getSocketFactory());
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);


            //send HTTP get request
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF8"));            
            wr.write("http get rquest");
            wr.flush();

            // read response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));           
            String string = null;

            while ((string = rd.readLine()) != null) {
                System.out.println(string);
                System.out.flush();
            }

            rd.close();
            wr.close();

            // Close connection.
            //sslSock.close();

        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
