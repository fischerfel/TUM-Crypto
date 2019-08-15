public class EncryptDecryptTwo {
static {
    try {
        Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
        field.setAccessible(true);
        field.set(null, java.lang.Boolean.FALSE);
    } catch (Exception ex) {
    }
}

private static String keyEnc = "BtDMQ7RfNVoRzJ7GYE32";

// Performs Encryption
public static String encrypt(String plainText) throws Exception {
    String passphrase = keyEnc;
    byte[] iv = DatatypeConverter.parseHexBinary("2aba86027a6f79dd463b81b0539bacb5");
    byte[] salt = DatatypeConverter.parseHexBinary("0f3d1b0d514ca313");
    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
    SecretKeySpec sKey = (SecretKeySpec) generateKeyFromPassword(passphrase, salt);

    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    c.init(Cipher.ENCRYPT_MODE, sKey, ivParameterSpec);
    byte[] encryptedData = c.doFinal(plainText.getBytes());
    return DatatypeConverter.printBase64Binary(encryptedData);

}

public static SecretKey generateKeyFromPassword(String password, byte[] saltBytes) throws GeneralSecurityException {

    KeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltBytes, 1, 256);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    SecretKey secretKey = keyFactory.generateSecret(keySpec);

    return new SecretKeySpec(secretKey.getEncoded(), "AES");
}

public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
}
}
