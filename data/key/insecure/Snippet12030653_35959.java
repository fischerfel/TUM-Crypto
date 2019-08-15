import sun.misc.BASE64Encoder;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Storage {
    public static String awsAccessKey = "xxxxxxxxxxxxxxx";
    public static String awsSecretKey = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    static String policy_document = "{\"expiration\": \"2013-01-01T00:00:00Z\","
            + "\"conditions\": ["
            + "{\"bucket\": \"kanta-assets\"},"
            + "[\"starts-with\", \"$key\", \"\"],"
            + "{\"acl\": \"public\"},"
            + "{\"success_action_redirect\": \"http://localhost/\"},"
            + "[\"starts-with\", \"$Content-Type\", \"\"],"
            + "[\"content-length-range\", 0, 1048576]" + "]" + "}";

    public static String getPolicy() throws UnsupportedEncodingException {
        return (new BASE64Encoder()).encode(policy_document.getBytes("UTF-8"))
                .replaceAll("\n", "").replaceAll("\r", "");
    }

    public static String getSignature() throws InvalidKeyException,
            IllegalStateException, NoSuchAlgorithmException,
            UnsupportedEncodingException {
        Mac hmac = Mac.getInstance("HmacSHA1");
        hmac.init(new SecretKeySpec(awsSecretKey.getBytes("UTF-8"), "HmacSHA1"));
        return (new BASE64Encoder()).encode(
                hmac.doFinal(getPolicy().getBytes("UTF-8"))).replaceAll("\n",
                "");

    }

}
