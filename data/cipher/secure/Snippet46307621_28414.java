package decryption;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DecryptAES256 {

    private static String salt;
    private static byte[] iv;
    private static byte[] encryptedMessageAndTag;
    private static byte[] key;

    public static void main(String[] args) {
        String key = "123456789aabbccddeefffffffffffff";
        String sourceText = "zMX8Xp8lCLGP3FsF7dy1uEODFG0+lhpoWR+xZPpNAXm2D39+CJUK5Kk0z4NbDfb/WbP8lHVWcTOuXf8hRA1AmtEV2G5kP3SH3mrGbyf4QthR4aOTqEQQAvt1T8LlIkBlgx32gehP/nwwm3DYyJV+NnN21Ac17L4=";
        System.out.println(decrypt(key, sourceText));
    }

    public static String decrypt(String masterkey, String encryptedText) {
        // decode encryptedText 
        encryptedText = new String(Base64.getDecoder().decode(encryptedText.getBytes()));

        // extract the different parts
        byte[] parts = encryptedText.getBytes();
        salt = new String(Arrays.copyOfRange(parts, 0, 64)); // not using for testing purposes
        iv = Arrays.copyOfRange(parts, 64, 76);
        encryptedMessageAndTag = Arrays.copyOfRange(parts, 76, parts.length);
        try {
            key = masterkey.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // not going to reach here
        }

        // call helper method to decrypt
        byte[] decipheredText = decodeAES_256_CBC();
        return new String(decipheredText);
    }

    private static byte[] decodeAES_256_CBC() {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec params = new GCMParameterSpec(128, iv, 0, iv.length);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, params);
            return cipher.doFinal(encryptedMessageAndTag);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to decrypt");
        }
    }
}
