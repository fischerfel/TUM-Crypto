package com.company.ProjectName;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ImageEncoder {

    public static String getImage(String imageUrl)
            throws MalformedURLException, IOException {

        String imageDataString = "";
        URL url = null;
        int i;
        try {
            url = new URL(imageUrl);
            System.out.println(imageUrl);



            HttpURLConnection connection = null; 
            String protocol = url.getProtocol(); 
            System.out.println(protocol);
          // this is to trust any certificates from the target server 
          if("https".equalsIgnoreCase(protocol)){ 
                      // Create a trust manager that does not validate certificate chains 
              System.out.println("inside If");
                      TrustManager[] trustAllCerts = new TrustManager[]{ 
                          new X509TrustManager() { 
                              public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
                                  return null; 
                              } 
                              public void checkClientTrusted( 
                                  java.security.cert.X509Certificate[] certs, String authType) { 
                              } 
                              public void checkServerTrusted( 
                                  java.security.cert.X509Certificate[] certs, String authType) { 
                              } 
                          } 
                      }; 

                      // Install the all-trusting trust manager 
                SSLContext sc = SSLContext.getInstance("SSL"); 
                sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); 

              connection = (HttpsURLConnection)url.openConnection(); 
              System.out.println("connection"+connection.getContentLength()); 
                }else{ 
                  connection=(HttpURLConnection) url.openConnection(); 

          } 


            InputStream input = connection.getInputStream(); 


            byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(input);

            input.close();
            imageDataString = encodeImage(bytes);

            return imageDataString;


        } catch (MalformedInputException malformedInputException) {
            malformedInputException.printStackTrace();
            imageDataString = malformedInputException.toString();
            return ("exception while reading the imag <" + imageDataString + ">");
        } catch (IOException ioException) {
            ioException.printStackTrace();
            imageDataString = ioException.toString();
            return ("exception while reading the imag <" + imageDataString + ">");
        }  catch (NoSuchAlgorithmException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace();
            imageDataString = e.toString();
            return ("exception while reading the imag <" + imageDataString + ">");
    } catch (KeyManagementException e) { 
    // TODO Auto-generated catch block 
    e.printStackTrace(); 
    imageDataString = e.toString();
    return ("exception while reading the imag <" + imageDataString + ">");
} 



    public static String encodeImage(byte[] imageData) {
        // TODO Auto-generated method stub
        org.apache.commons.codec.binary.Base64 base = new org.apache.commons.codec.binary.Base64(
                false);
        return base.encodeToString(imageData);

    }
}
