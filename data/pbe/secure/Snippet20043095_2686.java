private static byte[] unwrapKey(byte[] toUnwrap, String key) throws Exception {
    byte[] decoded = Base64.decode(toUnwrap);
    if (decoded == null || decoded.length <= 16) {
        throw new RuntimeException("Bad input data.");
    }
    byte[] salt = new byte[16];
    byte[] wrappedKey = new byte[decoded.length - 16];
    System.arraycopy(decoded, 0, salt, 0, 16);
    System.arraycopy(decoded, 16, wrappedKey, 0, decoded.length - 16);
    PBEKeySpec pbeKeySpec = new PBEKeySpec(key.toCharArray());
    SecretKey wrapperKey = SecretKeyFactory.getInstance("PBEWITHSHA256AND256BITAES-CBC-BC").generateSecret(pbeKeySpec);
    PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, 10);
    Cipher decCipher = Cipher.getInstance("AES/GCM/NoPadding", bcProvider);
    decCipher.init(Cipher.UNWRAP_MODE, wrapperKey, parameterSpec);
    return decCipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY).getEncoded();
}
