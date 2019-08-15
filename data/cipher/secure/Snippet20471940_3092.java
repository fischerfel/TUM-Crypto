private final byte[] salt = new SecureRandom().generateSeed(8);
SecretKeyFactory fact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec spec = new PBEKeySpec(null, salt, 65536, 256);
SecretKey tempsecret = fact.generateSecret(spec);
private SecretKey secret = new SecretKeySpec(tempsecret.getEncoded(), "AES");

private Cipher enccipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
enccipher.init(Cipher.ENCRYPT_MODE, secret);
private final byte[] iv = enccipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();

private Cipher deccipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
deccipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));

protected byte[] doEncrypt(String pass){
    return enccipher.doFinal(pass.getBytes());
}
protected String doDecrypt(byte[] ciphertext) {
    return new String (deccipher.doFinal(ciphertext), "UTF8");
}
