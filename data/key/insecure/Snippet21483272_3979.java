public class Mcrypt {

private SecretKeySpec keyspec;
private Cipher cipher;

private String SecretKey = "M02cnQ51Ji97vwT4";

public Mcrypt() {
    keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
    try {
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    }
}

public String encrypt(String text) throws Exception {
    if (text == null || text.length() == 0)
        throw new Exception("Empty string");
    byte[] encrypted = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec );
            encrypted = cipher.doFinal(padString(text).getBytes());
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }
    return Base64.encodeBase64String(encrypted);
}

public byte[] decrypt(String code) throws Exception {
    if (code == null || code.length() == 0)
        throw new Exception("Empty string");
    byte[] decrypted = null;

    try {
        cipher.init(Cipher.DECRYPT_MODE, keyspec );
        decrypted = cipher.doFinal(new Base64().decode(code.getBytes()));
    } catch (Exception e) {
        throw new Exception("[decrypt] " + e.getMessage());
    }
    return decrypted;
}

private static String padString(String source) {
    char paddingChar = ' ';
    int size = 16;
    int x = source.length() % size;
    int padLength = size - x;
    for (int i = 0; i < padLength; i++) {
        source += paddingChar;
    }
    return source;
}
}
