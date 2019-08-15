 public String encrypt(String value) throws GeneralSecurityException {
    Cipher encryptCipher = Cipher.getInstance("DES");
    SecretKeyFactory  keyFactory = SecretKeyFactory.getInstance("DES");
    String salt="";
    SecretKey sk = keyFactory.generateSecret(new DESKeySpec(salt.getBytes()));
    encryptCipher.init(Cipher.ENCRYPT_MODE, sk);
    return new String(Base64.encodeBase64(encryptCipher.doFinal(value.getBytes())));
}
