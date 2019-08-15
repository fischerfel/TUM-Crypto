package client;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import my.web.services.KeysGeneration;

public class TestClient {
    private static String keypass = "KHU12345KHU12345";
    public static void callPostService()
    {
        try{
            URL url = new URL("http://localhost:8080/RestWebService/MM");
            HttpURLConnection conn =(HttpURLConnection)url.openConnection();
            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            String text;
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            String email="sabah@gmail.com";

//          //**********AES Encryption


//           // Create key and cipher
            SecretKey aesKey = KeysGeneration.getKeys(keypass);

                Cipher cipher = Cipher.getInstance("AES");
//              // encrypt the text
                cipher.init(Cipher.ENCRYPT_MODE, aesKey);
                byte[] encrypted = cipher.doFinal(email.getBytes());
                System.out.println("checking encryption "+new String(encrypted));
               writer.write("&email="+(encrypted));

//               //**********AES Encryption 

////                //**********AES Decryption
//
////                // decrypt the text
                cipher.init(Cipher.DECRYPT_MODE, aesKey);
                String decrypted= new String(cipher.doFinal(encrypted));
                System.out.println("checking dencryption "+decrypted);
////
//////                   //**********AES Decryption

            writer.flush();
            writer.close();
            InputStream stream = conn.getInputStream();
            byte[] b = new byte[1024];
            int len = 0;
            while((len=stream.read(b, 0,1024))>0)
            {
                System.out.println(new String(b,0,len));
            }
            stream.close();

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void callGETService()
    {
        try{
            URL url = new URL("http://localhost:8080/RestWebService/MM?email=sabah@khu.ac.kr&pwd=1985");
            HttpURLConnection conn =(HttpURLConnection)url.openConnection();
            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(false);

            // Don't use a cached copy.
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            InputStream stream = conn.getInputStream();
            byte[] b = new byte[1024];
            int len = 0;
            while((len=stream.read(b, 0,1024))>0)
            {
                System.out.println(new String(b,0,len));
            }

            stream.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {
        // open your connection
        TestClient.callGETService();
        TestClient.callPostService();
    }

}

//webService

package my.web.services;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/MM")
public class WebService {

    private String keypass = "KHU12345KHU12345";

    //********GET Method*************

    @GET
    //GET method provides read only access to resources
    public String validateUserGETMethod(@QueryParam("email")String email, @QueryParam("pwd")String pwd)

    {
        return "Using GET method \n Email address: "+email+ " and password:" +pwd;
    }


    //********POST Method*************

    //POST method is used to create or update a new resources
    //To generate encryption of email parameter

    @POST
    @Path("/encryptEmail")
    public String validateUserPOSTMethodEncryption(@FormParam("email")String email)throws Exception

    {
        // Create key and cipher
        SecretKey aesKey = KeysGeneration.getKeys(keypass);
        Cipher cipher = Cipher.getInstance("AES");
        // encrypt the text
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(email.getBytes());
        System.err.println(new String(encrypted));

        return "Using POST method \n Email address (encrypted): "+new String(encrypted);

    }


    //POST method is used to create or update a new resources
    //To  generate decryption of email parameter
    @POST
    @Path("/decryptEmail")
    public String validateUserPOSTMethodDecryption(@FormParam("email")String email)throws Exception

    {
        //SecretKey aesKey = KeysGeneration.getKeys(keypass);
        SecretKey secKey = KeysGeneration.getKeys(keypass);//generator.generateKey();

        // Create key and cipher
        Cipher cipher = Cipher.getInstance("AES");


        // decrypt the text
        cipher.init(Cipher.DECRYPT_MODE, secKey);
        String decrypted= new String(cipher.doFinal(email.getBytes()));

        return "Using POST method \n Email address (decrypted): "+decrypted;
    }

}
