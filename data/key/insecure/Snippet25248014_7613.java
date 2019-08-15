import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.codehaus.jettison.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException


public class Connection {

private static WebResource baseResource;

private static final MediaType responseType = MediaType.APPLICATION_JSON_TYPE;


public Connection() throws NoSuchAlgorithmException {

    Client client = Client.create();
    baseResource = client.resource("https://www.bitstamp.net/api/");

}


public void test() throws NoSuchAlgorithmException, InvalidKeyException {

    String nonce_unixTime = String.valueOf(System.currentTimeMillis() / 1000L);
    String clientID = "xxx";
    String key = "yyy";
    String secret = "zzz";
    String message = nonce_unixTime + clientID + key;

    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    sha256_HMAC.init(secret_key);
    byte[] hash = sha256_HMAC.doFinal(message.getBytes());
    String signature = DatatypeConverter.printHexBinary(hash).toUpperCase();

    // Fetch the resource.
    JSONObject json = baseResource.path("balance/")
            .queryParam("key", key)
            .queryParam("signature", signature)
            .queryParam("nonce", nonce_unixTime)
            .accept(responseType).post(JSONObject.class);

    System.out.println(json.toString());
}
}
