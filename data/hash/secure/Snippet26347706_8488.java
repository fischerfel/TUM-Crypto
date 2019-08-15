import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class KrakenClient {

    protected static String key = "myAPIKey";     // API key
    protected static String secret = "MySecret====";  // API secret
    protected static String url = "api.kraken.com";     // API base URL
    protected static String version = "0"; // API version


    public static void main(String[] args) throws Exception {
        queryPrivateMethod("Balance");
    }

    public static void queryPrivateMethod(String method) throws NoSuchAlgorithmException, IOException{

        long nonce = System.currentTimeMillis();

        String path = "/" + version + "/private/" + method; // The path like "/0/private/Balance"

        String urlComp = "https://"+url+path; // The complete url like "https://api.kraken.com/0/private/Balance"

        String postdata = "nonce="+nonce;

        String sign = createSignature(nonce, path, postdata);

        postConnection(urlComp, sign, postdata);
    }

    /**
     * @param nonce
     * @param path
     * @param postdata
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static String createSignature(long nonce, String path,
            String postdata) throws NoSuchAlgorithmException, IOException {

        return hmac(path+sha256(nonce + postdata),  new String(Base64.decodeBase64(secret)));
    }

    public static String sha256Hex(String text) throws NoSuchAlgorithmException, IOException{
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(text);
    }

    public static byte[] sha256(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes());
        byte[] digest = md.digest();

        return digest;
    }

    public static void postConnection(String url1, String sign, String postData) throws IOException{

        URL url = new URL( url1 );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.addRequestProperty("API-Key", key);
        connection.addRequestProperty("API-Sign", Base64.encodeBase64String(sign.getBytes()));
        //      connection.addRequestProperty("API-Sign", sign);
        connection.addRequestProperty("User-Agent", "Mozilla/4.0");
        connection.setRequestMethod( "POST" );
        connection.setDoInput( true );
        connection.setDoOutput( true );
        connection.setUseCaches( false );
        //      connection.setRequestProperty( "Content-Type",
        //              "application/x-www-form-urlencoded" );
        connection.setRequestProperty( "Content-Length", String.valueOf(postData.length()) );

        OutputStreamWriter writer = new OutputStreamWriter( connection.getOutputStream() );
        writer.write( postData );
        writer.flush();


        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()) );

        for ( String line; (line = reader.readLine()) != null; )
        {
            System.out.println( line );
        }

        writer.close();
        reader.close();
    }


    public static String hmac(String text, String secret){

        Mac mac =null;
        SecretKeySpec key = null;

        // Create a new secret key
        try {
            key = new SecretKeySpec( secret.getBytes( "UTF-8"), "HmacSHA512" );
        } catch( UnsupportedEncodingException uee) {
            System.err.println( "Unsupported encoding exception: " + uee.toString());
            return null;
        }
        // Create a new mac
        try {
            mac = Mac.getInstance( "HmacSHA512" );
        } catch( NoSuchAlgorithmException nsae) {
            System.err.println( "No such algorithm exception: " + nsae.toString());
            return null;
        }

        // Init mac with key.
        try {
            mac.init( key);
        } catch( InvalidKeyException ike) {
            System.err.println( "Invalid key exception: " + ike.toString());
            return null;
        }


        // Encode the text with the secret
        try {

            return new String( mac.doFinal(text.getBytes( "UTF-8")));
        } catch( UnsupportedEncodingException uee) {
            System.err.println( "Unsupported encoding exception: " + uee.toString());
            return null;
        }
    }
}
