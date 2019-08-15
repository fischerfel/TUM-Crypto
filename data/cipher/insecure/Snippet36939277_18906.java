import java.util.Base64;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.util.Arrays;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HelloWorld {
    public static final String PHONENUMBER_PARAM = "phoneNumber";
    public static final String PIN_PARAM ="pin";

    public static final String MERCHANTID_PARAM = "merchantId";

    public static void main(String args[]) throws Exception {

    String phoneNumber ="+917738995286";
    String pin ="5577";

    String merchantId ="527425858";
    String encodedKey ="vPDkdTDrcygLPROzd1829A==";

    String payLoad = PHONENUMBER_PARAM + "=" +         URLEncoder.encode(phoneNumber, "UTF-8")+ "&" + PIN_PARAM + "=" + URLEncoder.encode(pin, "UTF-8") ;

    byte[] decodedKey = Base64.getDecoder().decode(encodedKey.getBytes());

    Key encryptionKey = new SecretKeySpec(decodedKey, "AES");

    byte[] utf8Bytes = payLoad.getBytes("utf-8");

    byte[] encryptedBody = encrypt(encryptionKey, utf8Bytes);
    String encryptedData = new  String(Base64.getEncoder().encode(encryptedBody));

    System.out.println("encryptedData:" + encryptedData);
 }
private static byte[] encrypt(Key encryptionKey, byte[] data) throws Exception {
    Cipher c = Cipher.getInstance("AES");
    c.init(1, encryptionKey);
    return c.doFinal(data);
}
}
