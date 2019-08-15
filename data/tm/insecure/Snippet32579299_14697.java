package com.softtouch.com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Security;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.apache.commons.codec.binary.Base64;

public class SoftTouchConnection {
  public static void main(String[] args) {
    try {           

    System.out.println(System.getProperty("java.version"));
    System.setProperty("javax.net.debug", "ssl");
    System.setProperty("sun.net.ssl.checkRevocation", "false"); 
       java.lang.System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
    Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
    SSLContext sc = SSLContext.getInstance("SSL");//TLSv1.2
    sc.init(null, new TrustManager[] {new TrustAllX509TrustManager() }, null);//new java.security.SecureRandom()

    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

        @Override
        public boolean verify(String string, SSLSession ssls) {                 
            System.out.println("test HttpsURLConnection");
            return true;
        }           
    });

    String userCredentialsnew = "abcdefghij01234567890klmnopqrstuvwxyz987";//"bearer abcdefghij01234567890klmnopqrstuvwxyz987";
    String basicAuth = "Basic "+ new String(new Base64().encode(userCredentialsnew.getBytes()));; //+ new String(new Base64().encode(userCredentials.getBytes()));
    URL hh= new URL("https://api.softtouch.eu/#/xxxxxxxx/#/customers?take=100");
    HttpsURLConnection conn = (HttpsURLConnection)hh.openConnection();


    conn.setSSLSocketFactory(sc.getSocketFactory());
    conn.setHostnameVerifier( new HostnameVerifier() {

        @Override
        public boolean verify(String string, SSLSession ssls) {             
            System.out.println("test conn");
            return true;
        }           
    });         

    conn.setDoInput(true);
    conn.setDoOutput(false);
    conn.setUseCaches(false);
    conn.setRequestMethod("GET");           
    conn.setRequestProperty ("Authorization", basicAuth);       
    conn.connect();


    BufferedReader inn = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;          
    while ((inputLine = inn.readLine()) != null) {
        System.out.println(inputLine);
    }         

   } catch (Exception e) {
      e.printStackTrace();
   }

  }
 }



package com.softtouch.com;

import javax.net.ssl.X509TrustManager;

import java.security.cert.X509Certificate;



public class TrustAllX509TrustManager implements X509TrustManager {
  public X509Certificate[] getAcceptedIssuers() {
    System.out.println("test1");
    //return new X509Certificate[0];
    return null;
 }

  public void checkClientTrusted(java.security.cert.X509Certificate[] certs,String authType) {
   System.out.println("test2");
 }

 public void checkServerTrusted(java.security.cert.X509Certificate[] certs,String authType) {
   System.out.println("test3");
 }

}
