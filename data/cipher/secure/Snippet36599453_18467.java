public static String EncryptString(String strToBeEncrypted) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException
{
    String modulusString = "qqoWhMwGrrEBRr92VYud3j+iIEm7652Fs20HvNckH3tRDJIL465TLy7Cil8VYxJre69zwny1aUAPYItybg5pSbSORmP+hMp6Jhs+mg3qRPvHfNIl23zynb4kAi4Mx/yEkGwsa6L946lZKY8f9UjDkLJY7yXevMML1LT+h/a0a38=";
    String publicExponentString = "AQAB";
    byte[] modulusBytes = Base64.decodeBase64(modulusString);
    byte[] exponentBytes = Base64.decodeBase64(publicExponentString);
    BigInteger modulus = new BigInteger(1, modulusBytes);
    BigInteger publicExponent = new BigInteger(1, exponentBytes);
    RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, publicExponent);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    PublicKey pubKey = fact.generatePublic(rsaPubKey);
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    byte[] plainBytes = strToBeEncrypted.getBytes("US-ASCII");
    byte[] cipherData = cipher.doFinal(plainBytes);
    String encryptedStringBase64 = Base64.encodeBase64String(cipherData);

    return encryptedStringBase64;
}
