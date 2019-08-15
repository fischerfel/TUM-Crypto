package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class Internet {

    static String key="password";
    static String account="klabs";
private static Base64 base64 ; 
private static String createAuthorizationHeader(String canonicalizedString) throws InvalidKeyException, Base64DecodingException, NoSuchAlgorithmException, IllegalStateException, UnsupportedEncodingException     {  
          Mac mac = Mac.getInstance("HmacSHA256");  
          mac.init(new SecretKeySpec(base64.decode(key), "HmacSHA256"));  
          String authKey = new String(base64.encode(mac.doFinal(canonicalizedString.getBytes("UTF-8"))));  
          String authStr = "SharedKey " + account + ":" + authKey;  
          return authStr;  
     } 

public static void main(String[] args) {

System.out.println("INTERNET");
String key="password";
String account="klabs";
long blobLength="Dipanshu Verma wrote this".getBytes().length;
File f = new File("C:\\Users\\Dipanshu\\Desktop\\abc.txt");
String requestMethod = "PUT";  
String urlPath = "delete";
String storageServiceVersion = "2009-09-19";


SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:sss");  
fmt.setTimeZone(TimeZone.getTimeZone("UTC"));  
String date = fmt.format(Calendar.getInstance().getTime()) + " UTC";
String blobType = "BlockBlob"; 
String canonicalizedHeaders = "x-ms-blob-type:"+blobType+"\nx-ms-date:"+date+"\nx-ms-version:"+storageServiceVersion;  
String canonicalizedResource = "/"+account+"/"+urlPath;  

String stringToSign = requestMethod+"\n\n\n"+blobLength+"\n\n\n\n\n\n\n\n\n"+canonicalizedHeaders+"\n"+canonicalizedResource;

try {
    String authorizationHeader = createAuthorizationHeader(stringToSign);
    URL myUrl = new URL("https://klabs.blob.core.windows.net/" + urlPath);

    HttpURLConnection connection=(HttpURLConnection)myUrl.openConnection();
    connection.setRequestProperty("x-ms-blob-type", blobType);
    connection.setRequestProperty("Content-Length", String.valueOf(blobLength));
    connection.setRequestProperty("x-ms-date", date);
    connection.setRequestProperty("x-ms-version", storageServiceVersion);
    connection.setRequestProperty("Authorization", authorizationHeader);
    connection.setDoOutput(true);
    connection.setRequestMethod("POST");
    System.out.println(String.valueOf(blobLength));
    System.out.println(date);
    System.out.println(storageServiceVersion);
    System.out.println(stringToSign);
    System.out.println(authorizationHeader);
    System.out.println(connection.getDoOutput());


    DataOutputStream  outStream = new DataOutputStream(connection.getOutputStream());


       // Send request
       outStream.writeBytes("Dipanshu Verma wrote this");
       outStream.flush();
       outStream.close();
       DataInputStream inStream = new DataInputStream(connection.getInputStream());
       System.out.println("BULLA");

       String buffer;
       while((buffer = inStream.readLine()) != null) {
           System.out.println(buffer);
       }

       // Close I/O streams
       inStream.close();
       outStream.close();





} catch (InvalidKeyException | Base64DecodingException | NoSuchAlgorithmException | IllegalStateException | UnsupportedEncodingException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
} catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}
}

}
