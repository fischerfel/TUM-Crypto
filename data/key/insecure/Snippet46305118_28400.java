package decryption;

import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DecryptAES256 {

    private static String salt;
    private static byte[] ivBase64;
    private static String base64EncryptedText;
    private static String key;

    public static void main(String[] args) {
        String key = "123456789aabbccddeefffffffffffff";
        String sourceText = "Q10blKuyyYozaRf0RVYW7bave8mT5wrJzSdURQQa3lEqEQtgYM3ss825YpCQ70A7hpq5ECPafAxdLMSIBZCxzGbv/Cj4i6W4JCJXuS107rUy0tAAQVQQA2ZhbrQ0gNV9QA==";
        System.out.println(decrypt(key, sourceText));
    }

    public static String decrypt(String masterkey, String encryptedText) {
        byte[] parts = encryptedText.getBytes();
        salt = new String(Arrays.copyOfRange(parts, 0, 64));
        ivBase64 = Arrays.copyOfRange(parts, 64, 76);
        ivBase64 = Base64.getDecoder().decode(ivBase64);
        base64EncryptedText = new String(Arrays.copyOfRange(parts, 76, parts.length));
        key = masterkey;
        byte[] decipheredText = decodeAES_256_CBC();
        return new String(decipheredText);
    }

    private static byte[] decodeAES_256_CBC() {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] text = base64EncryptedText.getBytes();
            GCMParameterSpec params = new GCMParameterSpec(128, ivBase64, 0, ivBase64.length);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, params);
            return cipher.doFinal(Base64.getDecoder().decode(text));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to decrypt");
        }
    }
}
