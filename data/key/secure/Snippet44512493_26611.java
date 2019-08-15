import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

private String hash_hmac(String str, String secret) throws Exception{
Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
byte[] string = str.getBytes();
String stringInBase64 = Base64.encodeToString(string, Base64.DEFAULT);
SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
sha256_HMAC.init(secretKey);
String hash = Base64.encodeToString(sha256_HMAC.doFinal(stringInBase64.getBytes()), Base64.DEFAULT);
return hash;
}

String str = "1234";
String key = "1234";

try {

    Log.d("HMAC:", hash_hmac(str,key));

} catch (Exception e) {
    Log.d("HMAC:","stop");
    e.printStackTrace();
}
