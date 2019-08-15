import java.net.URLEncoder;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public static String sign(String path, String accesKeyId, Long expires, String key) throws Exception {
 String canonical = "GET\n\n\n" + expires + "\n" + path;
 byte[] keyBytes = Base64.decodeBase64(key.getBytes("US-ASCII"));
 Mac hmac = Mac.getInstance("HmacSHA256");
 hmac.init(new SecretKeySpec(keyBytes, "HmacSHA256"));
 byte[] signatureBytes = hmac.doFinal(canonical.getBytes("UTF-8"));
 String signatureBase64 = new String(Base64.encodeBase64(signatureBytes), "US-ASCII");
 String signature = URLEncoder.encode(signatureBase64, "UTF-8");
return signature;
}
