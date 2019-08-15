import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class GetSasToken {

    static String sasToken = "";

    public static void main(String[] args) {

        sasToken = GetSASToken(<resouce url>, <your sas_keyname>,
                <your sas_key>);

        try {
            // your business logic


        } catch (Exception e) {
            e.printStackTrace();
            sasToken = GetSASToken("jaygong.servicebus.windows.net/test", "RootManageSharedAccessKey",
                    "tASE61OxG4Ci00rfI0Q56NKNXGxvNL5tRSrBZkhTjgI=");
            // retry your business logic
        }

    }

    private static String GetSASToken(String resourceUri, String keyName, String key) {
        long epoch = System.currentTimeMillis() / 1000L;
        int time = 60 * 20;
        String expiry = Long.toString(epoch + time);

        String sasToken = null;
        try {
            String stringToSign = URLEncoder.encode(resourceUri, "UTF-8") + "\n" + expiry;
            String signature = getHMAC256(key, stringToSign);
            sasToken = "SharedAccessSignature sr=" + URLEncoder.encode(resourceUri, "UTF-8") + "&sig="
                    + URLEncoder.encode(signature, "UTF-8") + "&se=" + expiry + "&skn=" + keyName;

            System.out.println("sasToken : " + sasToken);
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        return sasToken;
    }

    public static String getHMAC256(String key, String input) {
        Mac sha256_HMAC = null;
        String hash = null;
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            Encoder encoder = Base64.getEncoder();

            hash = new String(encoder.encode(sha256_HMAC.doFinal(input.getBytes("UTF-8"))));

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return hash;
    }

}
