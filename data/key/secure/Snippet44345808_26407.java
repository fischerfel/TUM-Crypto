public static String secretKeyToString(SecretKey key) {
  return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
}

public static SecretKey encodedStringToSecretKey(String encodedKey) {
  byte[] decodedKey = Base64.decode(encodedKey, Base64.DEFAULT);
  return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
}
