String base64;
char[] password = "password".toCharArray();
String randomString = new BigInteger(130, new SecureRandom()).toString(32);
try {
    //Encrypt Client Side
    SecretKey key = new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512").generateSecret(new PBEKeySpec(password)).getEncoded(), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    base64 = Base64.getEncoder().encodeToString(cipher.doFinal(randomString.getBytes(StandardCharsets.UTF_8)));
} catch (GeneralSecurityException e) {
    throw new IllegalStateException(e);
}
try {
    //Decrypt Server Side
    SecretKey key = new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512").generateSecret(new PBEKeySpec(password)).getEncoded(), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, key);
    //Check if both strings match
    System.out.println(Arrays.equals(cipher.doFinal(Base64.getDecoder().decode(base64)), randomString.getBytes(StandardCharsets.UTF_8)));
} catch (GeneralSecurityException e) {
    throw new IllegalStateException(e);
}
