public static String generateKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
    KeyGenerator generator = KeyGenerator.getInstance("AES");
    generator.init(128);
    Key key = generator.generateKey();
    return encodeKeyBase64(key);
}

public static String encodeKeyBase64(Key key) {
    return Base64.getEncoder().encodeToString(key.getEncoded());
}

public static Key decodeKeyBase64(String encodedKey) {
    byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
    return new SecretKeySpec(keyBytes, ALGORITHM);
}
