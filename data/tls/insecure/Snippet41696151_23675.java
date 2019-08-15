import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;


public class TestElk {

public static void main(String[] args) throws ClientProtocolException, IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnrecoverableKeyException, NoSuchProviderException {


    KeyStore clientStore = KeyStore.getInstance("PKCS12");
    clientStore.load(new FileInputStream(new File("C:/path_to_pfx/mypfx.pfx")), "pfxPass".toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(clientStore, "pfxPass".toCharArray());
    KeyManager[] kms = kmf.getKeyManagers();

    // Assuming that you imported the CA Cert "Subject: CN=MBIIS CA, OU=MBIIS, O=DAIMLER, C=DE"
    // to your cacerts Store.
    KeyStore trustStore = KeyStore.getInstance("JKS");
    trustStore.load(new FileInputStream("cacerts"), "changeit".toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);
    TrustManager[] tms = tmf.getTrustManagers();

    final SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kms,tms,new SecureRandom());
    SSLContext.setDefault(sslContext);

    HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;   

    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

    URL url = new URL("https://server-link");
    //System.setProperty("http.proxyHost", "53.88.72.33");
    //System.setProperty("http.proxyPort", "3128");             
    System.setProperty("https.proxyHost", "53.54.242.1");   //53.54.242.1   //53.88.72.33
    System.setProperty("https.proxyPort", "3128");

    HttpsURLConnection con =    (HttpsURLConnection)url.openConnection();           
    con.setRequestMethod("POST");
    con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
    con.setConnectTimeout(10000);
    con.setSSLSocketFactory(sslContext.getSocketFactory()); 
    con.connect();

    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = br.readLine()) != null) {
        sb.append(line+"\n");
    }
    br.close();
    System.out.println(sb.toString());
    //int s= con.getResponseCode();
}

}
