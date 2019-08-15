 import org.apache.commons.codec.binary.Base64;
 import org.apache.log4j.Logger;
 import javax.crypto.*;
 import javax.crypto.spec.IvParameterSpec;
 import javax.crypto.spec.PBEKeySpec;
 import javax.crypto.spec.SecretKeySpec;
 import java.io.UnsupportedEncodingException;
 import java.lang.reflect.Field;
 import java.lang.reflect.Modifier;
 import java.security.InvalidAlgorithmParameterException;
 import java.security.InvalidKeyException;
 import java.security.NoSuchAlgorithmException;
 import java.security.spec.InvalidKeySpecException;
 import java.security.spec.InvalidParameterSpecException;
 import java.security.spec.KeySpec;

 public class AESEncrypter {

private static final Logger LOGGER = Logger.getLogger(AESEncrypter.class);
private static final byte[] SALT = {
    (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
    (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
};
private static final int ITERATION_COUNT = 65536;
private static final int KEY_LENGTH = 256;
private Cipher ecipher;
private Cipher dcipher;

public AESEncrypter(String passPhrase)  {
    SecretKeyFactory factory = null;
    Field field = null;
    try {
        // hack for JCE unlimited strength policy jar installations
        field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, false);


        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, secret);

        dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = ecipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
        dcipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        LOGGER.error("Invalid Key Exception",e);
    } catch (InvalidAlgorithmParameterException e) {
        LOGGER.error("Invalid Algorithm Parameter Exception",e);
    } catch (NoSuchPaddingException e) {
        LOGGER.error("No Such Padding Exception",e);
    } catch (InvalidParameterSpecException e) {
        LOGGER.error("Invalid Parameter Exception",e);
    } catch (InvalidKeySpecException e) {
        LOGGER.error("Invalid Key Spec Exception",e);
    } catch (NoSuchFieldException e) {
        LOGGER.error("No Such field Exception",e);
    } catch (ClassNotFoundException e) {
        LOGGER.error("Class Not Found Exception",e);
    } catch (IllegalAccessException e) {
        LOGGER.error("Illegal Argument Exception",e);
    }


}

public String encrypt(String encrypt) {
    byte[] bytes = new byte[0];
    byte[] encrypted = new byte[0];
    try {
        bytes = encrypt.getBytes("UTF-8");
        encrypted = encrypt(bytes);
    } catch (Exception e) {
        LOGGER.error("Exception",e);
    }
    return Base64.encodeBase64String(encrypted);
}

private byte[] encrypt(byte[] plain)  {
    try {
        return ecipher.doFinal(plain);
    } catch (IllegalBlockSizeException e) {
        LOGGER.error("Illegal Block Size Exception",e);
    } catch (BadPaddingException e) {
        LOGGER.error("Bad Padding Exception",e);
    }
    return null;
}

public String decrypt(String encrypt) throws UnsupportedEncodingException {
    byte[] bytes = Base64.decodeBase64(encrypt.getBytes("UTF-8"));
        byte[] decrypted = decrypt(bytes);
        return new String(decrypted);
}

public byte[] decrypt(byte[] encrypt)  {
    try {
        return dcipher.doFinal(encrypt);
    } catch (IllegalBlockSizeException e) {
        LOGGER.error("Illegal Block Size Exception",e);
    } catch (BadPaddingException e) {
        LOGGER.error("Bad Padding Exception",e);
    }
    return null;
}




public static void main(String[] args) throws UnsupportedEncodingException {
        String password = "F1C0T0N83LL34";
        String message = "{\"version\":\"1.0\",\"rccCubeType\":\"TCR_CASE_D\",\"timeZone\":\"Asia/Kolkata\",\"timeOffset\":\"+05:30\",\"tenant\":\"0001\",\"extractionTime\":\"20170713162718117\"}";
        AESEncrypter encrypter = new AESEncrypter(password);
        String cipher = encrypter.encrypt(message);
        System.out.println(cipher);
        System.out.println(new String(encrypter.decrypt("I8YbMaRvAw+rzPQu//uXnDDFrk/EtscXpcxBzqonVOpJ1VjvpwtRGwrsEz9R1rroC95Vj9bzPDbkX2qdLXK4jLKlzaoINXOxF+dHslnBVl3xG61qh9QdCuMTBzEEw18K51JJu+13bjuUO20+0uZiY5q6Wg1sQ60C0QEeO/7K9F/TSUN1r5l02Q9NSDQJpkvlglHZEfPJ7ST4179oqlQUjQ==").getBytes("UTF-8")));
     }
 }
