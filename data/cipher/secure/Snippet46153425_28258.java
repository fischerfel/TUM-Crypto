private final int GCM_IV_LENGTH = 12;
private final int GCM_TAG_LENGTH = 16;

private static String encrypt(String privateString, SecretKey skey) {
    byte[] iv = new byte[GCM_IV_LENGTH];
    (new SecureRandom()).nextBytes(iv);

    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
    cipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec);

    byte[] ciphertext = cipher.doFinal(privateString.getBytes("UTF8"));
    byte[] encrypted = new byte[iv.length + ciphertext.length];
    System.arraycopy(iv, 0, encrypted, 0, iv.length);
    System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);

    Base64Encoder encoder = new Base64Encoder();
    String encoded = encoder.encode(encrypted);

    return encoded;
}

private static String decrypt(String encrypted, SecretKey skey) {
    Base64Decoder decoder = new Base64Decoder();
    String decoded = encoder.encode(encrypted);

    byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);

    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
    cipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);

    byte[] ciphertext = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);

    String newString = new String(ciphertext, "UTF8");

    return newString;
}
