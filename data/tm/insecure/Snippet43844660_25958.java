import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;

public class Rest{
private static final String USER_AGENT =  "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";
//private static final String USER_AGENT = "Mozilla/5.0";
private static String name = "admin";
private static String password = "password";

private static String authString = name + ":" + password;
private static byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
private static String authStringEnc = new String(authEncBytes);

private static String GET_URL = "https://127.0.0.1:8080/storage";
private static String PUT_URL_PREFIX = "https://127.0.0.1:8080/storage/";
private static String PUT_URL = "";
private static String DELETE_URL_PREFIX = "https://127.0.0.1:8080/storage/";
private static String  DELETE_URL = "";
private static String HEAD_URL = "https://127.0.0.1:8080/storage";

private static int deleteCount = 10;
private static int getCount = 10;
private static int headCount = 10;
private static int putCount = 10;

private static int deleteBytes = 0;
private static int getBytes = 0;
private static int headBytes = 0;
private static int putBytes = 0;

private static char[] data = new char[20];

public static void main(String args[]) throws IOException{


    TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers(){
                return null;
            }
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType){
                //No need to implement.
            }
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType){
                //No need to implement.
            }
        }
    };

    // Install the all-trusting trust manager
    try 
    {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        disableSSL();

        for(int i = 0; i < putCount; i++){
            PUT_URL = PUT_URL_PREFIX + i;
            System.out.println("PUT URL: " + PUT_URL);
            sendPUT();
            System.out.println("PUT Done");
            putBytes += data.length;
        }

        for(int i = 0; i < getCount; i++){
            sendGET();
            System.out.println("GET Done");
        }

        for(int i = 0; i < deleteCount; i++){
            DELETE_URL = DELETE_URL_PREFIX + i;
            System.out.println("DELETE URL: " + DELETE_URL);
            sendDELETE();
            System.out.println("DELETE Done");
        }

        for(int i = 0; i < headCount; i++){
            sendHEAD();
            System.out.println("HEAD Done");
        }

        System.out.println("\nPUT operation count = " + putCount + "\nPUT Bandwidth = " + putBytes);
        System.out.println();
        System.out.println("\nGET operation count = " + getCount + "\nGET Bandwidth = " + getBytes);
        System.out.println();
        System.out.println("\nDELETE operation count = " + deleteCount + "\nDELETE Bandwidth = " + deleteBytes);
        System.out.println();
        System.out.println("\nHEAD operation count = " + headCount + "\nHEAD Bandwidth = " + headBytes);
        System.out.println();

    } 
    catch (Exception e) 
    {
        System.out.println(e);
    }
}

public static void sendGET() throws IOException{

    URL obj = new URL(GET_URL);
    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
    con.setRequestProperty("Authorization", "Basic " + authStringEnc);
    con.setRequestMethod("GET");

    con.setRequestProperty("User-Agent", USER_AGENT);
    con.setRequestProperty("Content-Type", "application/xml");
    int responseCode = con.getResponseCode();
    System.out.println("GET Response Code :: " + responseCode);
    if (responseCode == HttpURLConnection.HTTP_OK) { // success
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append("\n" + inputLine);
        }
        in.close();

        // print result
        System.out.println(response.toString());
    } else {
        System.out.println("GET request didn't work");
    }
}

private static void sendPUT() throws IOException {
    Arrays.fill(data, '1');
    String str = new String(data);
    URL obj = new URL(PUT_URL);
    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
    con.setRequestProperty("Authorization", "Basic " + authStringEnc);
    con.setRequestMethod("PUT");
    con.setRequestProperty("User-Agent", USER_AGENT);

    // For PUT only - START
    con.setDoOutput(true);
    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    wr.writeBytes(str);
    wr.flush();
    wr.close();
    // For PUT only - END

    int responseCode = con.getResponseCode();
    System.out.println("PUT Response Code :: " + responseCode);

    if (responseCode == HttpURLConnection.HTTP_OK) { //success
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        System.out.println(response.toString());
    } else {
        System.out.println("PUT request didn't work");
    }

}

private static void sendDELETE() throws IOException{

    URL obj = new URL(DELETE_URL);
    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    con.setRequestMethod("DELETE");
    con.setRequestProperty("User-Agent", USER_AGENT);
    con.setDoOutput(true);

    int responseCode = con.getResponseCode();
    System.out.println("DELETE Response Code :: " + responseCode);

}

public static void sendHEAD() throws IOException{

    URL obj = new URL(HEAD_URL);
    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
    con.setRequestMethod("HEAD");

    con.setRequestProperty("User-Agent", USER_AGENT);
    int responseCode = con.getResponseCode();
    System.out.println("HEAD Response Code :: " + responseCode);
    if (responseCode == HttpURLConnection.HTTP_OK) { // success

        Map<String, List<String>> map = con.getHeaderFields();


        System.out.println("Printing All Response Header for URL: " + obj.toString() + "\n");
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        System.out.println("\nGet Response Header By Key ...\n");
        List<String> contentLength = map.get("Content-Length");
        if (contentLength == null) {
            System.out.println("'Content-Length' doesn't present in Header!");
        } else {
            for (String header : contentLength) {
                System.out.println("Content-Lenght: " + header);
            }
        }
    } else {
        System.out.println("HEAD request didn't work");
    }
}
private static void disableSSL() {
      try {
         TrustManager[] trustAllCerts = new TrustManager[] { new   MyTrustManager() };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
