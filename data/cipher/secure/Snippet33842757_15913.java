import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import java.nio.charset.Charset;
import java.lang.reflect.Field;

public class Cryptage_EAS {

    public String decrypt(String encryptedText, String salt, String hash, String vector) throws Exception {

        // Autoriser le cryptage EAS 256
        try {
            Field field = Class.forName("javax.crypto.JceSecurity").
            getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, java.lang.Boolean.FALSE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Set en tableau de byte des différentes entrées
        byte[] vectorBytes = vector.getBytes(Charset.forName("UTF-8"));
        byte[] saltBytes = salt.getBytes(Charset.forName("UTF-8"));
        byte[] encryptedTextBytes = Base64.decodeBase64(encryptedText);

        // Création de la clé
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(hash.toCharArray(), saltBytes, 1, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        // Décryptage
        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(vectorBytes));

        return new String(cipher.doFinal(encryptedTextBytes), "UTF-8");
    }

    public String encrypt(String textToCrypt, String salt, String hash, String vector) throws Exception {  

        // Autoriser le cryptage EAS 256
        try {
            Field field = Class.forName("javax.crypto.JceSecurity").
            getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, java.lang.Boolean.FALSE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Set en tableau de byte des différentes entrées
        byte[] vectorBytes = vector.getBytes(Charset.forName("UTF-8"));
        byte[] saltBytes = salt.getBytes(Charset.forName("UTF-8"));

        // Création de la clé
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(hash.toCharArray(), saltBytes, 1, 256);
        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        // Cryptage
        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(vectorBytes));
        byte[] encryptedTextBytes = cipher.doFinal(textToCrypt.getBytes("UTF-8"));

        return new Base64().encodeAsString(encryptedTextBytes);
    }
}
