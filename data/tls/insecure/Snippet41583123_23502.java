       import java.net.HttpURLConnection;
       import java.net.URL;
       import java.security.KeyManagementException;
       import java.security.KeyStore;
       import java.security.KeyStoreException;
       import java.security.NoSuchAlgorithmException;
       import java.security.NoSuchProviderException;
       import java.security.cert.Certificate;
       import java.security.cert.CertificateException;
       import java.io.*;
       import javax.net.ssl.HttpsURLConnection;
       import javax.net.ssl.SSLContext;

    import java.net.*;
    import java.util.HashMap;
    import java.util.Iterator;
    import java.util.Map;
    import java.io.*;
    import javax.net.ssl.HostnameVerifier;
    import javax.net.ssl.HttpsURLConnection;
    import javax.net.ssl.SSLSession;
    import javax.net.ssl.TrustManagerFactory;


   public class httpclint { 

   public static void main(String[] args) throws IOException,      KeyManagementException, KeyStoreException, NoSuchProviderException,    NoSuchAlgorithmException, CertificateException {
    // TODO Auto-generated method stub      

    final String urlString = "https://xxx/xx"; 

    URL url = new URL(urlString);
    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();


    con.setRequestProperty("Content-Type", "text/plain; charset=\"utf8\"");
    con.setRequestMethod("POST");

    //certificate
    KeyStore ks = KeyStore.getInstance("Windows-ROOT", "SunMSCAPI");
    ks.load(null,null);
    Certificate cer = ks.getCertificate("alias_name");

      TrustManagerFactory tmf = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore k = KeyStore.getInstance(KeyStore.getDefaultType());
        k.load(null);           
        k.setCertificateEntry("Cert", cer);         
        tmf.init(k);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        con.setSSLSocketFactory(sslContext.getSocketFactory()); 



        System.out.println("Resp Code:"+con .getResponseCode()); 
        System.out.println("Resp Message:"+ con .getResponseMessage()); 

    //BASE64Encoder encoder = new BASE64Encoder();
    //String encoded = encoder.encode((authentication).getBytes("UTF-8"));
    //connection.setRequestProperty("Authorization", "Basic " + encoded);

}
