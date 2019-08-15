import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
 import java.security.NoSuchAlgorithmException;
 import java.util.Arrays;
 import javax.net.ssl.HttpsURLConnection;
 import javax.net.ssl.SSLContext;
 import javax.net.ssl.SSLParameters;

public class TlsCheck {

/**
 * test whether this client can connect to TLS v1.2 or not
 */

public static boolean isSuccessfulTLS12connection(){
    try{
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        URL url = new URL("https://tlstest.paypal.com");
        HttpsURLConnection httpsConnection = (HttpsURLConnection)url.openConnection();
        httpsConnection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpsConnection.getInputStream()));
        StringBuilder body = new StringBuilder();
        while(reader.ready()){
            body.append(reader.readLine());
        }
        httpsConnection.disconnect();
        System.out.println("The body is::"+body.toString());
        if(body.toString().equals("PayPal_Connection_OK")){
            return true;
        }
    }catch(NoSuchAlgorithmException ne){
        ne.printStackTrace();
    }catch(UnknownHostException ue){
        ue.printStackTrace();
    }catch(IOException ioe){
        ioe.printStackTrace();
    }catch(KeyManagementException ke){
        ke.printStackTrace();
    }
    return false;
}

public static void main(String args[]){
    try{
        SSLParameters sslParams = SSLContext.getDefault().getSupportedSSLParameters();
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        sslParams.setProtocols(new String[] { "TLSv1.2"});
        String[] protocols = sslParams.getProtocols();
        System.out.println("The Supported Protocols are::"+Arrays.asList(protocols));

    }catch(NoSuchAlgorithmException ne){
        ne.printStackTrace();
    }

    if(isSuccessfulTLS12connection()){
        System.out.println("The connection to TLS v1.2 endpoint is succesful");
    }else{
        System.out.println("The connection to TLS v1.2 failed!");
    }
}
