import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AzureSasTokenCreator
{

    public static void main(String[] args) throws InvalidKeyException, UnsupportedEncodingException,
                    MalformedURLException, NoSuchAlgorithmException
    {
        String token = generateSasTokenForIotDevice("myiothub.azure-devices.net/devices/mydevice",
                        "ZNILSsz4ke0r5DQ8rfB/PBWf6QqWGV7aaT/iICi9WTc=", 3600);

        System.out.println(token);
    }

    private static String generateSasTokenForIotDevice(String uri, String devicePrimaryKey, int validtySeconds)
                    throws UnsupportedEncodingException, MalformedURLException, NoSuchAlgorithmException,
                    InvalidKeyException
    {
        Date now = new Date();
        Date previousDate = new Date(1970);
        long tokenExpirationTime = ((now.getTime() - previousDate.getTime()) / 1000) + validtySeconds;

        String signature = getSignature(uri, tokenExpirationTime, devicePrimaryKey);

        String token = String.format("SharedAccessSignature sr=%s&sig=%s&se=%s", uri, signature,
                        String.valueOf(tokenExpirationTime));

        return token;
    }

    private static String getSignature(String resourceUri, long expiryTime, String devicePrimaryKey)
                    throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException
    {
        byte[] textToSign = new String(resourceUri + "\n" + expiryTime).getBytes();
        byte[] decodedDeviceKey = Base64.getDecoder().decode(devicePrimaryKey);
        byte[] signature = encryptHmacSha256(textToSign, decodedDeviceKey);
        byte[] encryptedSignature = Base64.getEncoder().encode(signature);
        String encryptedSignatureUtf8 = new String(encryptedSignature, StandardCharsets.UTF_8);

        return URLEncoder.encode(encryptedSignatureUtf8, "utf-8");
    }

    private static byte[] encryptHmacSha256(byte[] textToSign, byte[] key)
                    throws NoSuchAlgorithmException, InvalidKeyException

    {
        SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
        Mac hMacSha256 = Mac.getInstance("HmacSHA256");
        hMacSha256.init(secretKey);
        return hMacSha256.doFinal(textToSign);
    }
}
