public String decryptWithSymmetricKey(String encryptedHexData, String symmKey, String symmPadding) throws Exception {

    byte[] key = Hex.decodeHex(symmKey.toCharArray());
    SecretKey skeySpec = new SecretKeySpec(key, DESEDE);
    IvParameterSpec iv = new IvParameterSpec(new byte[8]);

    Cipher cipher = Cipher.getInstance(symmPadding);
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv); // mac: Wrong key size

    byte[] decoded = Hex.decodeHex(encryptedHexData.toCharArray());
    byte[] deciphered = cipher.doFinal(decoded); // windows: Given final block not properly padded

    return new String(deciphered);
}
