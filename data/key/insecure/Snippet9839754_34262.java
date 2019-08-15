public String decrypt(byte[] pCryptedStringtoDecrypt) throws Exception{
    byte[] key = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    System.arraycopy(this.passphrase.getBytes(), 0, key, 0, this.passphrase.getBytes().length);
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
    byte[] encrypted = pCryptedStringtoDecrypt;
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] original = cipher.doFinal(encrypted);
    String originalString = new String(original);
    return originalString;
}
