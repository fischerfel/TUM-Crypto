public encrypt(String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH); //256 bit
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
    this.ecipher = Cipher.getInstance("AES");
    this.ecipher.init(Cipher.ENCRYPT_MODE, secret);
    byte[] bytes = encrypt.getBytes("UTF-8");
    byte[] encrypted = this.ecipher.doFinal(bytes);
    return Base64.encodeBase64String(encrypted);
}
