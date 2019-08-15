public class AES64 {
public static String encrypt(String key, String ivs, String value) {
    try {
        IvParameterSpec iv = new IvParameterSpec(ivs.getBytes("ISO-8859-1"));

        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("ISO-8859-1"),
                "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(value.getBytes("ISO-8859-1"));
        String r = Base64.encodeToString(encrypted, Base64.DEFAULT);
        return r;
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null;
}

public static String decrypt(String key, String ivs, String encrypted) {
    try {
        IvParameterSpec iv = new IvParameterSpec(ivs.getBytes("ISO-8859-1"));

        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("ISO-8859-1"),
                "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(Base64.decode(encrypted.getBytes("ISO-8859-1"), Base64.DEFAULT));

        return new String(original);

    } catch (Exception ex) {
        ex.printStackTrace();
        return ex.toString();
    }
  }
}
