public class myAES {
private static final String key = "4ff539a893fed04840749287bb3e4152";
private static final String initVector = "79f564e83be16711759ac7c730072bd0";
private final  static char[] hexArray = "0123456789ABCDEF".toCharArray();

public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}

public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
}

public static byte[] encrypt(String value) {
    try {
        IvParameterSpec iv = new IvParameterSpec(hexStringToByteArray(initVector));
        SecretKeySpec skeySpec = new SecretKeySpec(hexStringToByteArray(key), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] encrypted = cipher.doFinal(value.getBytes());
        Log.v("Encryption successful", bytesToHex(encrypted));
        return encrypted;
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null;
}

public static String decrypt(byte[] encrypted) {
    try {
        IvParameterSpec iv = new IvParameterSpec(hexStringToByteArray(initVector));
        SecretKeySpec skeySpec = new SecretKeySpec(hexStringToByteArray(key), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] original = cipher.doFinal(encrypted);
        Log.v("Decryption successful", new String(original, "UTF-8"));
        return new String(original);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null;
}
}
