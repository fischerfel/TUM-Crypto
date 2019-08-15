import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


public class OAuthForWooCommerce {

private static String key = "consumer_Key";
private static String secret = "consumer_Secret";

private static final String HMAC_SHA1 = "HmacSHA1";

private static final String ENC = "UTF-8";

private static Base64 base64 = new Base64();


private static String getSignature(String url, String params)
        throws UnsupportedEncodingException, NoSuchAlgorithmException,
        InvalidKeyException {
    /**
     * base has three parts, they are connected by "&": 1) protocol 2) URL
     * (need to be URLEncoded) 3) Parameter List (need to be URLEncoded).
     */
    StringBuilder base = new StringBuilder();
    base.append("GET&");
    base.append(url);
    base.append("&");
    base.append(params);
    System.out.println("String for oauth_signature generation:" + base);
    // yea, don't ask me why, it is needed to append a "&" to the end of
    // secret key.
    byte[] keyBytes = (secret + "&").getBytes(ENC);

    SecretKey key = new SecretKeySpec(keyBytes, HMAC_SHA1);

    Mac mac = Mac.getInstance(HMAC_SHA1);
    mac.init(key);

    // encode it, base64 it, change it to string and return.
    return new String(base64.encode(mac.doFinal(base.toString().getBytes(
            ENC))), ENC).trim();
}


public static void main(String[] args) throws ClientProtocolException,
        IOException, URISyntaxException, InvalidKeyException,
        NoSuchAlgorithmException {
     System.out.println("*** Welcome to WooCommerce Klipfolio integration Wizard ***");
    HttpClient httpclient = new DefaultHttpClient();
    List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    // These params should ordered in key
    //qparams.add(new BasicNameValuePair("oauth_callback", "oob"));
    qparams.add(new BasicNameValuePair("oauth_consumer_key", key));
    String nonce = RandomStringUtils.randomAlphanumeric(32);
    //String nonce2 = URLEncoder.encode(nonce1, "UTF-8");
    qparams.add(new BasicNameValuePair("oauth_nonce", nonce));
    //qparams.add(new BasicNameValuePair("oauth_nonce", ""+ (int) (Math.random() * 100000000)));
    qparams.add(new BasicNameValuePair("oauth_signature_method",
            "HMAC-SHA1"));
    qparams.add(new BasicNameValuePair("oauth_timestamp", ""
            + (System.currentTimeMillis() / 1000)));
    qparams.add(new BasicNameValuePair("oauth_version", "1.0"));

    // generate the oauth_signature
    String signature = getSignature(URLEncoder.encode(
            "http://MY_END_URL/wc-api/v2/orders", ENC),
            URLEncoder.encode(URLEncodedUtils.format(qparams, ENC), ENC));
    System.out.println("Getting Oauth Signature...");
    // add it to params list
    qparams.add(new BasicNameValuePair("oauth_signature", signature));

    // generate URI which lead to access_token and token_secret.
    URI uri = URIUtils.createURI("http", "MY_END _URL", -1,
            "wc-api/v2/orders",
            URLEncodedUtils.format(qparams, ENC), null);

    System.out.println("Connecting to  the URL : \n"
            + uri.toString());

    HttpGet httpget = new HttpGet(uri);
    // output the response content.
    System.out.println("Getting Response from the server :");

    HttpResponse response = httpclient.execute(httpget);
    HttpEntity entity = response.getEntity();
    if (entity != null) {
        InputStream instream = entity.getContent();
        int len;
        byte[] tmp = new byte[2048];
        while ((len = instream.read(tmp)) != -1) {
            System.out.println(new String(tmp, 0, len, ENC));
        }
    }
}

}
