package rest.test.first;


import java.io.*;
import java.net.*;
import java.security.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;


public class urlConnection{
  public static void main(String args[]) throws Exception {

     System.setProperty("javax.net.ssl.trustStore", "/usr/lib/jvm/jdk1.6.0_32/jre/lib/security/cacerts"); 
  System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
  Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

      //TrustStore..
      char[] passphrase = "changeit".toCharArray(); //password
      KeyStore keystore = KeyStore.getInstance("JKS");
      //KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      keystore.load(new FileInputStream("/usr/lib/jvm/jdk1.6.0_32/jre/lib/security/cacerts"), passphrase); //path

      //TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509"); //instance
      TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      tmf.init(keystore);


      SSLContext context = SSLContext.getInstance("TLS");
      TrustManager[] trustManagers = tmf.getTrustManagers();
      context.init(null, trustManagers, null);
      SSLSocketFactory sf = context.getSocketFactory();
      URL url = new URL("https://www.google.es");

      HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
      httpsCon.setSSLSocketFactory(sf);
      httpsCon.setRequestMethod("GET");

      /*InputStream inStrm = httpsCon.getInputStream();
      System.out.println("\nContent at " + url);
      int ch;
        while (((ch = inStrm.read()) != -1)){
          System.out.print((char) ch);
        inStrm.close();
      }*/

      System.out.println("Response Message is " + httpsCon.getResponseMessage());

 }
}
