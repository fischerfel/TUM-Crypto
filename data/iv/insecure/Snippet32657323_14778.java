private static String encryptDataWithSymmetricKey(String data, SecretKey secretKey, String symmPadding) throws Exception {
    Cipher cipher = Cipher.getInstance(symmPadding);
    IvParameterSpec iv = new IvParameterSpec(new byte[8]);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

    byte[] encrypted = cipher.doFinal(data.getBytes());
    char[] encoded = Hex.encodeHex(encrypted);
    return new String(encoded);
}
