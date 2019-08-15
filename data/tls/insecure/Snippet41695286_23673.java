import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import javax.net.ssl.TrustManagerFactory;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;


public class TestElk {

public static void main(String[] args) throws ClientProtocolException, IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnrecoverableKeyException, NoSuchProviderException {

    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
    Certificate certificate = certificateFactory.generateCertificate(new FileInputStream(new File("C:/Work/certi/jre1.8.0_91/lib/security/elkcert.cer")));//exported certificate

    /* KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
    ks.load(null,null);

    Enumeration enumeration = ks.aliases();     
    while(enumeration.hasMoreElements()) {            
        String alias = (String)enumeration.nextElement();
        System.out.println("alias name: " + alias);        }

    Certificate[] certificate = ks.getCertificateChain("alias");
     */

    // Create TrustStore        
    KeyStore trustStoreContainingTheCertificate =     KeyStore.getInstance(KeyStore.getDefaultType());
    trustStoreContainingTheCertificate.load(null, null);

    trustStoreContainingTheCertificate.setCertificateEntry("cert", certificate);

    // Create SSLContext
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(trustStoreContainingTheCertificate);


    final SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null,trustManagerFactory.getTrustManagers(),new SecureRandom());
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
    //int s= con.getResponseCode();  }
