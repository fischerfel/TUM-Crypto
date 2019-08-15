public String cryptString(String s) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
    byte[] KeyData = this.cryptKey.getBytes();
    SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(Cipher.ENCRYPT_MODE, KS);
    String ret = new String(cipher.doFinal(s.getBytes("UTF-8")));
    return ret;
}

public String decryptString(byte[] s) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    byte[] KeyData = this.cryptKey.getBytes();
    SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(Cipher.DECRYPT_MODE, KS);
    String ret = new String(cipher.doFinal(s));
    return ret;
}
