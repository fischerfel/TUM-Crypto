import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class BlowfishTwoWayHashImpl {

    static {
        test();
    }

    public static void test() {
        String key = "wibble";

        String passwordToEnrypt = "Aaaaaaa7";

        String enc = BlowfishTwoWayHashImpl.encryptBlowfish(passwordToEnrypt, key);
        System.out.println("'" + passwordToEnrypt + "' encrypted: '" + enc + "'");

        String dec = BlowfishTwoWayHashImpl.decryptBlowfish(enc, key);
        System.out.println("'" + passwordToEnrypt + "' decrypted: '" + dec + "'");
    }


    private static final String CIPHER_NAME = "Blowfish";

    public static String encryptBlowfish(String toEncrypt, String key) {
        return processString(toEncrypt, key, Cipher.ENCRYPT_MODE);
    }

    public static String decryptBlowfish(String toDecrypt, String key) {
        return processString(toDecrypt, key, Cipher.DECRYPT_MODE);
    }

    private static String processString(String toEncrypt, String key, int encryptDecryptMode) {

        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), CIPHER_NAME);

        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(encryptDecryptMode, secretKeySpec);
            return new String(cipher.doFinal(toEncrypt.getBytes()));
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

}
