package javaTests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.security.SecureRandom;

public class PrintContent {
  public static void main(String[] args) throws Exception {
    String https_url = "https://api.precharge.net/charge";
    URL url;
    url = new URL(https_url);

    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

    if (con != null) {
      try {

        System.out.println("****** Content of the URL ********");
        BufferedReader br =
            new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        String input;

        while ((input = br.readLine()) != null) {
          System.out.println(input);
        }
        br.close();

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void disableCertificates() {
    TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
          @Override
          public X509Certificate[] getAcceptedIssuers() {
            System.out.println("Warning! SSL validation has been disabled!");
            return null;
          }

          @Override
          public void checkServerTrusted(X509Certificate[] certs, String authType) {
            System.out.println("Warning! SSL validation has been disabled!");
          }

          @Override
          public void checkClientTrusted(X509Certificate[] certs, String authType) {
            System.out.println("Warning! SSL validation has been disabled!");
          }
        }
    };
    try {
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
      System.out.println("Failed to disable SSL validation," + e.getMessage());
    }
  }
}
