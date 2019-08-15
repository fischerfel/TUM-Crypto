package airtel.product.action;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.codec.binary.Base64;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;


public class HTTPSClient {
    public static void main(String[] args) {
        try {
            doHttpsPost("9711296535", "0.5", "0.5");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private static HttpsURLConnection getHTTPSConnection(String url) throws Exception{
        URL chargingUrl;

        HostnameVerifier hv = new HostnameVerifier(){
        public boolean verify(String urlHostName, SSLSession session) {
        System.out.println("Warning: URL Host: "+urlHostName+" vs. "+session.getPeerHost());
        return true;
        }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv); 
        KeyStore ks;
        HttpsURLConnection urlc=null;
        //try {
            chargingUrl = new URL(url);
            ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("/vishnu/twss/keystore/KEYSTORE.jks"), "12345678".toCharArray());//Location of keystore c:\\SDPKeystore.dat,12345678
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, "airtel@123".toCharArray()); //password is airtel@123
            TrustManager[] tm;
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);
            tm = tmf.getTrustManagers();
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(kmf.getKeyManagers(), tm, null);
            SSLSocketFactory sslSocketFactory =sslContext.getSocketFactory();
            urlc = (HttpsURLConnection) chargingUrl.openConnection();
            urlc.setSSLSocketFactory(sslSocketFactory); 
            //System.out.println("DoHttpPost(): Connection received");
            //System.out.println("DoHttpPost(): Setting UserName and Password");                        
            String usernamePassword = "vishnu" + ":" + "air@123";
            String encoding = null;

            byte[] encoded = Base64.encodeBase64(usernamePassword.getBytes());
            encoding = new String(encoded);

            urlc.setRequestProperty( "Authorization", "Basic " + encoding );
            urlc.setRequestMethod("POST");
            urlc.setUseCaches(false);
        return urlc;
    }

    public static HttpsURLConnection doHttpsPost(String msisdn, String actualPrice, String basePrice) throws MalformedURLException, Exception {

    System.out.println("dHTTPPost(): Start");
    String chargingServiceURL="https://10.5.45.111:6443/sdpchargingservice/http/charge";
    String strResponse = "";
    HttpsURLConnection urlc=getHTTPSConnection(chargingServiceURL);
    try{
        //get data from config file
        Properties prop = new Properties();
        prop.load(new FileInputStream("/vishnu/twss/config.properties"));

        System.out.println("HttpService :charge():Going to hit Charging Service..");
        Long currentMilis = System.currentTimeMillis();

        urlc.setRequestProperty("Keep-Alive", "300"); 
        urlc.setRequestProperty("Connection", "keep-alive"); 

        //set the keep aive in the system properties
        System.setProperty("http.keepAlive", "true");
        System.setProperty("http.maxConnections", "2000");

        urlc.setRequestMethod("POST");
        urlc.setRequestProperty("SOAPAction","");
        urlc.setRequestProperty("Content-Type","text/xml; charset=utf-8");
        urlc.setDoOutput(true);
        urlc.addRequestProperty("operation","debit");
        urlc.addRequestProperty("userId",msisdn); //CP to use his own msisdn
        urlc.addRequestProperty("contentId","Exam Results");
        urlc.addRequestProperty("itemName","Exam Results");
        urlc.addRequestProperty("contentDescription", "Exam Results"+currentMilis);
        urlc.addRequestProperty("contentMediaType","Exam Results");
        urlc.addRequestProperty("actualPrice",actualPrice);
        urlc.addRequestProperty("basePrice",basePrice);
        urlc.addRequestProperty("discountApplied","0");
        urlc.addRequestProperty("cpId","voicetap");
        urlc.addRequestProperty("eventType","Content Purchase");
        urlc.addRequestProperty("deliveryChannel","SMS");
        urlc.addRequestProperty("currency","INR");
        urlc.addRequestProperty("copyrightId","null");
        urlc.addRequestProperty("sMSkeyword","SMS");
        urlc.addRequestProperty("srcCode","606770");
        urlc.addRequestProperty("contentUrl","www.voicetap.com");
        urlc.addRequestProperty("copyrightDescription","Test Desc");
        System.out.println("going to hit service......");

       System.out.println(" urlc response  "+urlc.getResponseMessage());

       try{     
            System.out.println(urlc.getHeaderField("status"));
            String loggermsg = "MSISDN: "+prop.getProperty("userId") +
                    " Actualprice: "+prop.getProperty("actualPrice") +
                    " transactionId: "+urlc.getHeaderField("transactionId") +
                    " status: "+urlc.getHeaderField("status")+
                    " errorCode: "+urlc.getHeaderField("errorCode")+
                    " date: "+new java.util.Date();
          InputStreamReader isr = new InputStreamReader(urlc.getInputStream());
          BufferedReader in = new BufferedReader(isr);
          String inputLine;
          while ((inputLine = in.readLine()) != null){
            System.out.println(inputLine);
          } 
          in.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Execption raised during parsing SOAP-response :::" + e.getMessage());
        }
    }
    catch (Exception ex) {
        ex.printStackTrace();
        System.out.println("Error "+ex);
    }
    return urlc;
}
}
