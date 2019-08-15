public byte[] crypt(byte[] toCrypt) throws Exception {
    byte[] key = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    System.arraycopy(this.passphrase.getBytes(), 0, key, 0, ((this.passphrase.getBytes().length < 16) ? this.passphrase.getBytes().length : 16));
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(toCrypt);
    return encrypted;
}
