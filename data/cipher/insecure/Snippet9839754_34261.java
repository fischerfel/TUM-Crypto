public byte[] crypt(String pStringToCrypt) throws Exception{

    byte[] key = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    System.arraycopy(this.passphrase.getBytes(), 0, key, 0, this.passphrase.getBytes().length);
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(pStringToCrypt.getBytes());
    return encrypted;

}
