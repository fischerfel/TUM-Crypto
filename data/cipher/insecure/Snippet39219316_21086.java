private static char[] ENCRYPTION_PASSWORD
     = "some password populated by configuration".toCharArray();

public static String encrypt(String mystring)
  throws GeneralSecurityException, UnsupportedEncodingException {
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    SecretKey key = keyFactory.generateSecret(new PBEKeySpec(ENCRYPTION_PASSWORD));
    Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
    pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
    return DatatypeConverter
        .printBase64Binary(pbeCipher.doFinal(mystring.getBytes("UTF-8")));
}

public static String decrypt(String string)
  throws GeneralSecurityException, IOException {
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    SecretKey key = keyFactory.generateSecret(new PBEKeySpec(ENCRYPTION_PASSWORD));
    Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
    pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
    return new String(pbeCipher.doFinal(DatatypeConverter
                                           .parseBase64Binary(estring)), "UTF-8");
}
