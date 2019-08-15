public byte[] decryptt(byte[] toDecrypt) throws Exception {
    byte[] key = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    System.arraycopy(this.passphrase.getBytes(), 0, key, 0, ((this.passphrase.getBytes().length < 16) ? this.passphrase.getBytes().length : 16));
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] original = cipher.doFinal(toDecrypt);
    return original;
}
