private static final String ENCRYPTION_ALGORITHM = "AES/ECB/PKCS5Padding";
private static final SecureRandom RANDOM = new SecureRandom();

public static void main(String[] args) throws UnsupportedEncodingException, GeneralSecurityException {
    final KeyGenerator keyGen = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM.substring(0, ENCRYPTION_ALGORITHM.indexOf('/')));
    keyGen.init(128, RANDOM);
    final SecretKey key = keyGen.generateKey();
    final String s = "My topsecret string";
    System.out.println(s);
    final Cipher encryption = getCipher(key, Cipher.ENCRYPT_MODE);
    final String enc = DatatypeConverter.printBase64Binary(encryption.doFinal(s.getBytes("UTF-8")));
    System.out.println(enc);
    final Cipher decryption = getCipher(key, Cipher.DECRYPT_MODE);
    final String dec = new String(decryption.doFinal(DatatypeConverter.parseBase64Binary(enc)), "UTF-8");
    System.out.println(dec);
}

private static Cipher getCipher(final Key key, final int mode) throws GeneralSecurityException {
    final Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
    cipher.init(mode, key, RANDOM);
    return cipher;
}
