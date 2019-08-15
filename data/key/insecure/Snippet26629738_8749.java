import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
...

private static byte[] key = {
        0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    };  // "ThisIsASecretKey";

    public static String encrypt(String stringToEncrypt) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        final String encryptedString = Base64.encodeBase64String(cipher.doFinal(stringToEncrypt.getBytes()));
        return encryptedString;
    }

    public static String decrypt(String stringToDecrypt) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(stringToDecrypt)));
        return decryptedString;
    }
