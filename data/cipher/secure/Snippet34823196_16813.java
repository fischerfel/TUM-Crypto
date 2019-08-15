import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class CryptoLib {

    public CryptoLib() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public String encrypt(String plainText, String key) throws Exception {

        // convert key to bytes
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(key.getBytes("UTF-8"));

        byte[] keyBytes = md.digest();

        // Use the first 16 bytes (or even less if key is shorter)
        byte[] keyBytes16 = new byte[16];
        System.arraycopy(keyBytes, 0, keyBytes16, 0, Math.min(keyBytes.length, 64));

        System.arraycopy(keyBytes, 0, keyBytes16, 0,
                Math.min(keyBytes.length, 16));

        // convert plain text to bytes
        byte[] plainBytes = plainText.getBytes("UTF-8");

        // setup cipher
        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes16, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        byte[] iv = new byte[16]; // initialization vector with all 0
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv));

        // encrypt
        byte[] encrypted = cipher.doFinal(plainBytes);
        String encryptedString = new String(Base64.encodeBase64(cipher.doFinal(encrypted)));
        // encryptedString

        return encryptedString;
    }

    public String Decrypt(String EncryptedText, String key) throws Exception {
        // convert key to bytes
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(key.getBytes("UTF-8"));

        byte[] keyBytes = md.digest();
        // Use the first 16 bytes (or even less if key is shorter)
        byte[] keyBytes16 = new byte[16];

        System.arraycopy(keyBytes, 0, keyBytes16, 0, Math.min(keyBytes.length, 64));

        // convert plain text to bytes
        //  byte[] decodeBase64 = Base64.decodeBase64(EncryptedText);
        byte[] plainBytes = Base64.decodeBase64(EncryptedText.getBytes("UTF-8"));

        // setup cipher
        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes16, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        byte[] iv = new byte[16]; // initialization vector with all 0
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv));


        byte[] decrypteed = cipher.doFinal(plainBytes);


        return new String(decrypteed, "UTF-8");
    }
}
