package test;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Sign 
{
        public static void main(String args[]) throws Exception{
            String apikey="UETJV-Q_G1quXrV84WMHCRbDzWy-ZCUSdtmvsJWdLODMHbfIP4qLQt19CDj_4m1M5yvoZ4I18SdHsBXAtH5Hmw";
            String request=("apikey="+apikey+"&command=listZones").toLowerCase();
            String secret="8q0oR6oPIhB2je9mmgIuuaPJ5mBLkdzsL1BN6OVI8MTfkmW6Y9oHzvlpRiOr5cP612IWH_TTjvfMfKBgKgUMMg";
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(),"HmacSHA1");
            mac.init(keySpec);
            mac.update(request.getBytes());
            byte[] encryptedBytes = mac.doFinal();

            System.out.println(Base64.encodeBase64String(encryptedBytes));//result
            }
}
