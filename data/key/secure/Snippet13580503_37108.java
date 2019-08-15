public static byte[] encrypt(byte[] data, byte[] key) {
try {
    Cipher cipher = Cipher.getInstance("AES/CBC/ZeroBytePadding");
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
    byte[] empty = new byte[16]; // For better security you should use a random 16 byte key!!!
    IvParameterSpec ivps = new IvParameterSpec(empty);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivps);
    return cipher.doFinal(data);
} catch (Exception e) {
    // ...
}

return null;
}
