import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Aes { 
    private static final int pswdIterations = 10;
    private static final int keySize =  128;

    public static String encrypt(String plainText, String password, String salt, String initializationVector) throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] saltBytes = salt.getBytes("UTF-8");
        byte[] ivBytes = initializationVector.getBytes("UTF-8");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, pswdIterations, keySize);
        SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
        byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes());
        return new Base64().encodeAsString(encryptedTextBytes);
    } 
}
