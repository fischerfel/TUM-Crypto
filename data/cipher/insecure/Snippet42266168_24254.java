import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class DoDecode {
    private static final String DES_TYPE = "DES";
//    private static final String DES_TYPE = "DES/CBC/NoPadding";
//    private static final String DES_TYPE = "DES/CBC/PKCS5Padding";
//    private static final String DES_TYPE = "DES/ECB/NoPadding";
//    private static final String DES_TYPE = "DES/ECB/PKCS5Padding"; //Use this
//    private static final String DES_TYPE = "DESede/CBC/NoPadding";
//    private static final String DES_TYPE = "DESede/CBC/PKCS5Padding";
//    private static final String DES_TYPE = "DESede/ECB/NoPadding";
//    private static final String DES_TYPE = "DESede/ECB/PKCS5Padding";

    public synchronized static String encode(String unencodedString, String key) {
        String ret = null;

        try {
            DESKeySpec keySpec = new DESKeySpec(key.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey skey = keyFactory.generateSecret(keySpec);
            sun.misc.BASE64Encoder base64encoder = new BASE64Encoder();

            byte[] cleartext = unencodedString.getBytes("UTF8");

            Cipher cipher = Cipher.getInstance(DES_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, skey);

            ret = base64encoder.encode(cipher.doFinal(cleartext));
        } catch (Exception ex) {
            System.err.println("Encode exception: "+ex.getMessage());
        }

        return ret;
    }

    public static String decode(String encodedString, String key) {
        String ret = null;

        try {
            DESKeySpec keySpec = new DESKeySpec(key.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey skey = keyFactory.generateSecret(keySpec);
            sun.misc.BASE64Decoder base64decoder = new BASE64Decoder();

            byte[] encrypedPwdBytes = base64decoder.decodeBuffer(encodedString);

            Cipher cipher = Cipher.getInstance(DES_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, skey);
            byte[] plainTextPwdBytes = (cipher.doFinal(encrypedPwdBytes));

            ret = new String(plainTextPwdBytes);
        } catch (Exception ex) {
            System.err.println("Decode exception: " + ex.getMessage());
        }

        return ret;
    }

    private static final String wasValidStr = "h1JTFcRjW6vveQUrQqPUgnjGXo3NEZKDnBThZQN7uLfzPEpeFFONV4mvL71cT/xQb1mz5Xa/XZ/aW2GawZNumgO0reUZSDh30F7NfK0S/rMWM8FxcjBCkfFWAbLZHcyDJ5wW3F1yl5g=";

    public static void main(String[] args) {
        System.out.println(DoDecode.decode(wasValidStr, "invpwd~~"));

        String encoded = DoDecode.encode("This has worked in the past!", "invpwd~~");
        System.out.println(encoded);
        System.out.println(DoDecode.decode(encoded, "invpwd~~"));
    }
}
