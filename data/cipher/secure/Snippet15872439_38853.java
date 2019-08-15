public static final String UNICODE_FORMAT = "UTF8";

public static String encrypt(String Data, SecretKeySpec skeySpec,IvParameterSpec ivspec) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
        byte[] plainBytes = Data.getBytes(UNICODE_FORMAT);
        byte[] encrypted = cipher.doFinal(plainBytes);
        String encryption = bytesToString(encrypted);
        return encryption;
}

public static String decrypt(String encryptedData,SecretKeySpec skeySpec,IvParameterSpec ivspec) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
    byte[] decryptval = hexToBytes(encryptedData);
    byte[] decrypted = cipher.doFinal(decryptval);
    return new String(decrypted);
}

public static String bytesToString(byte[] bytes) {
    HexBinaryAdapter adapter = new HexBinaryAdapter();
    String s = adapter.marshal(bytes);
    return s;
}

public static byte[] hexToBytes(String hexString) {
    HexBinaryAdapter adapter = new HexBinaryAdapter();
    byte[] bytes = adapter.unmarshal(hexString);
    return bytes;
}
