package com.dell;    
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpURLConnectionExample {
    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {
        HttpURLConnectionExample http = new HttpURLConnectionExample();
        System.out.println("Testing 1 - Send Http GET request");
        http.sendGet();
        System.out.println("\nTesting 2 - Send Http POST request");
        http.sendPost();

    }

    // HTTP GET request
    private void sendGet() throws Exception {
        URL obj = null;
        TrustManager[] trustAllCerts = new TrustManager[] { 
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
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
        }
        try {
            obj = new URL("https://10.94.218.114");
        } catch (MalformedURLException e) {
        }

        HttpURLConnection con = (HttpURLConnection) obj.openConnection(); 
        con.setRequestMethod("GET");        
        con.setRequestProperty("User-Agent", USER_AGENT); 
        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode); 
        BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer(); 
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

    // HTTP POST request
    private void sendPost() throws Exception {

        String url = "https://selfsolve.apple.com/wcResults.do";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());

    }

}
