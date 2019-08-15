import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Lala {    
    static String policy_document = "{ \"expiration\": \"2015-12-30T12:00:00.000Z\"," +
            "  \"conditions\": [" +
            "    {\"bucket\": \"sigv4examplebucket\"}," +
            "    [\"starts-with\", \"$key\", \"user/user1/\"]," +
            "    {\"acl\": \"public-read\"}," +
            "    {\"success_action_redirect\": \"http://sigv4examplebucket.s3.amazonaws.com/successful_upload.html\"}," +
            "    [\"starts-with\", \"$Content-Type\", \"image/\"]," +
            "    {\"x-amz-meta-uuid\": \"14365123651274\"}," +
            "    {\"x-amz-server-side-encryption\": \"AES256\"}," +
            "    [\"starts-with\", \"$x-amz-meta-tag\", \"\"]," +
            "    {\"x-amz-credential\": \"AKIAIOSFODNN7EXAMPLE/20151229/us-east-1/s3/aws4_request\"}," +
            "    {\"x-amz-algorithm\": \"AWS4-HMAC-SHA256\"}," +
            "    {\"x-amz-date\": \"20151229T000000Z\" }" +
            "  ]" +
            "}";

    static String secret_key = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";

    public static void main(String[] args) throws Exception {
        // Create a policy using UTF-8 encoding.
        byte[] utf8_policy = policy_document.getBytes("UTF-8");

        // Convert the UTF-8-encoded policy bytes to Base64. The result is the StringToSign.
        String base64_policy = new String(Base64.encodeBase64(utf8_policy));

        // Create a signing key.
        byte[] signing_key = getSignatureKey(secret_key , "20151229", "us-east-1", "s3");

        // Use the signing key to sign the StringToSign using HMAC-SHA256 signing algorithm.
        byte[] signature_bytes = HmacSHA256(base64_policy, signing_key);
        String signature = Hex.encodeHexString(signature_bytes);

        System.out.println(base64_policy);
        System.out.println();
        System.out.println(signature);
    }

    static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception  {
        byte[] kSecret = ("AWS4" + key).getBytes("UTF-8");
        byte[] kDate    = HmacSHA256(dateStamp, kSecret);
        byte[] kRegion  = HmacSHA256(regionName, kDate);
        byte[] kService = HmacSHA256(serviceName, kRegion);
        byte[] kSigning = HmacSHA256("aws4_request", kService);
        return kSigning;
    }

    static byte[] HmacSHA256(String data, byte[] key) throws Exception  {
        String algorithm="HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data.getBytes("UTF-8"));
    }
}
