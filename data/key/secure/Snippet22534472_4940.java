import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;

public class Main {

public static void main(String[] args) {
    String key    = "apiKeyDEMO";
    String secret = "apiSecretDEMO";
    Integer nonce  = 100;
    String ID = "123456";

    String message = nonce.toString() + ID + key;

    String signature = "";
    try {
        signature = encode(secret, message);
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }


    Map<String, String> map = new LinkedHashMap<String, String>();

    map.put("key", key);
    map.put("signature", signature);
    map.put("nonce", nonce.toString());

      StringBuilder urlParameters = new StringBuilder();

      for (String k : map.keySet()) {
          if(urlParameters.length() > 0) {
              urlParameters.append("&");
          }
          urlParameters.append(k).append("=").append(URLEncoder.encode(map.get(k)));
      }

      //System.out.println(urlParameters);


    try {
        doPost(map);
    } catch (KeyManagementException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }



}

public static String encode(String secret, String message) throws Exception {

    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    sha256_HMAC.init(secret_key);
    byte[] hash = sha256_HMAC.doFinal(message.getBytes());
    String signature = DatatypeConverter.printHexBinary(hash).toUpperCase();

    //System.out.println(signature);

    return signature;

}


private static String doPost( Map<String,String> params) throws IOException, NoSuchAlgorithmException, KeyManagementException {

    //"param1=a&param2=b&param3=c"
      StringBuilder urlParameters = new StringBuilder();

      for (String key : params.keySet()) {
         if(urlParameters.length() > 0) {
            urlParameters.append("&");
         }
         //urlParameters.append(key).append("=").append(URLEncoder.encode(params.get(key)));
         urlParameters.append(key).append("=").append(params.get(key));
      }


      String query = urlParameters.toString();

      System.out.println(query);


      URL url = new URL("https://www.bitstamp.net/api/balance/");

      HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

      //add reuqest header
      con.setRequestMethod("POST");

      // Send post request
      con.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(con.getOutputStream());
      wr.writeBytes(query);
      wr.flush();
      wr.close();


      String json = "";
      if (con.getResponseCode() == HttpsURLConnection.HTTP_OK) {
         BufferedReader br = new BufferedReader(new
         InputStreamReader(con.getInputStream()));
         String text;
         while((text = br.readLine()) != null) {
            json += text;
         }
         br.close();
      }else{
         ;
      }
      con.disconnect();
      System.out.println("Code Response: " + con.getResponseCode());
      return json;


   }
