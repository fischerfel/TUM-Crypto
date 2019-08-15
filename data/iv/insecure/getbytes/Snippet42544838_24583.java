import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class Encryptor {

    private static final String password = "passwordKey";

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private static final byte [] salt = "test salt as string".getBytes(UTF_8);

    public static String encrypt(String text) throws UnsupportedEncodingException {

        IvParameterSpec ivSpec = new IvParameterSpec(password.getBytes(UTF_8));
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, 10000, ivSpec);
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 10000);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithSHA256AndAES_256");
        SecretKey key = secretKeyFactory.generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance("PBEWithSHA256AndAES_256");
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

        byte [] encrypted = cipher.doFinal(text.getBytes(UTF_8));
        return Base64.encodeBase64String(encrypted);
    }

    public static String decrypt(String encryptedText) throws UnsupportedEncodingException {

        IvParameterSpec ivSpec = new IvParameterSpec(password.getBytes(UTF_8));
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, 10000, ivSpec);
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 10000);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithSHA256AndAES_256");
        SecretKey key = secretKeyFactory.generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance("PBEWithSHA256AndAES_256");
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        byte [] decoded = Base64.decodeBase64String(encryptedText);
        byte [] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, UTF_8);
    }

    public static void main(String args []) throws UnsupportedEncodingException {
        String encryptedFromWindows = "eFRvTevgk/oslll+234r5tdsss==";
        System.out.println(decrypt(encryptedFromWindows));
    }
}
