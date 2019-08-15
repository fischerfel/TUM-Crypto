import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.sun.mail.util.BASE64DecoderStream;

public class Decryptor {

    private Cipher dcipher;

    private static final int iterationCount = 10;

    private static byte[] salt = {
        (byte) 0xB2, (byte) 0x12, (byte) 0xD5, (byte) 0xB2,
        (byte) 0x44, (byte) 0x21, (byte) 0xC3, (byte) 0xC3 };

    Decryptor(String passPhrase) {
        try {
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            dcipher = Cipher.getInstance(key.getAlgorithm());
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public String decrypt(String str) {
        try {
            byte[] dec = BASE64DecoderStream.decode(str.getBytes());
            byte[] utf8 = dcipher.doFinal(dec);
            return new String(utf8, "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
