package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GetBlob {


public static void main(String[] args) {





    String url="https://MYACCOUNT.blob.core.windows.net/MYCONTAINER/MYBLOB";


    try {
        System.out.println("RUNNIGN");
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();


 connection.setRequestProperty("Authorization", createQuery());
 connection.setRequestProperty("x-ms-version", "2009-09-19");

        InputStream response = connection.getInputStream();
        System.out.println("SUCCESSS");
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(response));
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }   



    } catch (IOException e) {
        e.printStackTrace();
    }

}
public static String createQuery()
{
    String dateFormat="EEE, dd MMM yyyy hh:mm:ss zzz";
    SimpleDateFormat dateFormatGmt = new SimpleDateFormat(dateFormat);
    dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));
    String date=dateFormatGmt.format(new Date());


    String Signature="GET\n\n\n\n\n\n\n\n\n\n\n\n" +
            "x-ms-date:" +date+
            "\nx-ms-version:2009-09-19" ;

            // I do not know CANOCALIZED RESOURCE 
            //WHAT ARE THEY??
//          +"\n/myaccount/myaccount/mycontainer\ncomp:metadata\nrestype:container\ntimeout:20";

    String SharedKey="SharedKey";
    String AccountName="MYACCOUNT";

    String encryptedSignature=(encrypt(Signature));

    String auth=""+SharedKey+" "+AccountName+":"+encryptedSignature;

        return auth;

}





public static String encrypt(String clearTextPassword)   {  
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(clearTextPassword.getBytes());
      return new sun.misc.BASE64Encoder().encode(md.digest());
    } catch (NoSuchAlgorithmException e) {
    }
    return "";
  }



}
