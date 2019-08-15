public String encrypt(String plainText, byte[] encryptionKey) throws Exception {
    SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");           
    cipher.init(Cipher.ENCRYPT_MODE, key, iV);
    byte[] data = new byte[iV.getIV().length + plainText.getBytes("UTF-8").length];
    // Merge together plain IV and encrypted cipher text
    System.arraycopy(iV.getIV(), 0, data, 0, iV.getIV().length);
    System.arraycopy(cipher.doFinal(plainText.getBytes("UTF-8")), 0, data, iV.getIV().length, plainText.getBytes("UTF-8").length);

    return Base64.encodeToString(data, Base64.DEFAULT);

}
