public static byte[] decrypt(byte[] input) throws Exception {
    byte[] rawKey = getKey("bla".getBytes());
    SecretKeySpec secretKeySpec = new SecretKeySpec(rawKey, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
    AlgorithmParameterSpec iv = new IvParameterSpec(new byte[] { 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
    return cipher.doFinal(input);
}
