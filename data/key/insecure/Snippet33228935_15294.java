import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
public class auth_4 {

//static final String TwitterUri = "https://api.twitter.com/oauth2/token";
static final String TwitterClientID = "xxxx";
static final String TwitterClientSecret = "xxxxx";
static final String TwitterRedirectUri = "https://twitter.com";

public static void main(String args[]){
    try{
        new auth_4().processTwitter();
    }catch(Exception ex){
        ex.printStackTrace();
    }
}

public void processTwitter() throws Exception {
    int millis = (int) System.currentTimeMillis() * -1;
    //int time = (int) millis / 1000;
    int time = (int)((System.currentTimeMillis())/1000);
    //First, we sort all the parameters used in our request and formulate a signature base string. 
    String signatureBaseString = "GET&" + 
    URLEncoder.encode(TwitterUri, "UTF-8")+ 
    "&" + "oauth_callback%3D"+ URLEncoder.encode(TwitterRedirectUri, "UTF-8")+ 
    "%26oauth_consumer_key%3D"+ URLEncoder.encode(TwitterClientID, "UTF-8")+ 
    "%26oauth_nonce%3D"+ URLEncoder.encode(String.valueOf(millis), "UTF-8")+ 
    "%26oauth_signature_method%3D"+ URLEncoder.encode("HMAC-SHA1", "UTF-8")+ 
    "%26oauth_timestamp%3D"+ URLEncoder.encode(String.valueOf(time), "UTF-8")+ 
    "%26oauth_version%3D" + URLEncoder.encode("1.0", "UTF-8");

    System.out.println("Signature Base: "+signatureBaseString);

    //Our signing key is (notice the dangling ampersand at the end):
    String signingKey = TwitterClientSecret + "&";
    String signature = null;
    try {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret = new SecretKeySpec(signingKey.getBytes(),
                "HmacSHA1");
        mac.init(secret);
        byte[] digest = mac.doFinal(signatureBaseString.getBytes());
        //We then use the composite signing key to create an oauth_signature from the signature base string
        signature = Base64.encodeBase64String(digest);
        System.out.println("The resultant oauth_signature is: " +signature);
    } catch (Exception ex1) {
        ex1.printStackTrace();
        System.exit(0);
    }

    //Now we just generate an HTTP header called "Authorization" with the relevant OAuth parameters for the request:
    String oAuthParameters = "OAuth " +
            "oauth_callback=\""+ URLEncoder.encode(TwitterRedirectUri, "UTF-8") + "\""
            + ", oauth_consumer_key=\""+ URLEncoder.encode(TwitterClientID, "UTF-8") + "\""
            + ", oauth_nonce=\""+ URLEncoder.encode(String.valueOf(millis), "UTF-8") + "\""
            + ", oauth_signature=\""+ URLEncoder.encode(signature, "UTF-8") + "\""
            + ", oauth_signature_method=\""+ URLEncoder.encode("HMAC-SHA1", "UTF-8") + "\""
            + ", oauth_timestamp=\""+ URLEncoder.encode(String.valueOf(time), "UTF-8") + "\""
            + ", oauth_version=\"" + URLEncoder.encode("1.0", "UTF-8")+ "\"";
    System.out.println("oAuth Parameters are " +oAuthParameters);

    //When Twitter.com receives our request, it will respond with an oauth_token, oauth_token_secret
    //SO LET ME SEE IF I AM LUCKY!!!
    URL url = new URL("https://ton.twitter.com...../image.png");
    HttpsURLConnection conn = null;
    try {
        conn = (HttpsURLConnection) url.openConnection();
        conn.addRequestProperty("User-Agent","Mozilla/5.0");
        //System.setProperty("http.agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:35.0) Gecko/20100101 Firefox/35.0"); 
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        //Set the oAuth params in header
        conn.setRequestProperty("Authorization", oAuthParameters);
        //Shall I set something in body?
        String bodyParams = 
                "oauth_callback="+ URLEncoder.encode(TwitterRedirectUri, "UTF-8")
                + "&oauth_consumer_key="+ URLEncoder.encode(TwitterClientID, "UTF-8") 
                + "&oauth_nonce="+ URLEncoder.encode(String.valueOf(millis), "UTF-8")
                + "&oauth_signature=" + URLEncoder.encode(signature, "UTF-8")
                + "&oauth_signature_method="+URLEncoder.encode("HMAC-SHA1", "UTF-8") 
                + "&oauth_timestamp="+URLEncoder.encode(String.valueOf(time), "UTF-8")
                + "&oauth_version=" + URLEncoder.encode("1.0", "UTF-8");
        System.out.println("Parameters for body are " +oAuthParameters);
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        writer.write(bodyParams);
        writer.flush();
        conn.connect();

        // Get Response
        InputStream is = conn.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer dataBack = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            dataBack.append(line);
            dataBack.append('\r');
        }
        rd.close();
    } catch (Exception e) {
        System.err.println("And blasted!");
        e.printStackTrace();
    } finally {
        if (conn != null) {
            conn.disconnect();
        }
    }

}
