// http://stackoverflow.com/questions/3451670/java-aes-and-using-my-own-key
public static byte[] generateKey(String key) throws GeneralSecurityException, UnsupportedEncodingException {
    byte[] binary = key.getBytes("UTF-8");
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    binary = sha.digest(binary);
    // Use only first 128 bit.
    binary = Arrays.copyOf(binary, 16);
    return binary;
}

// http://stackoverflow.com/questions/17322002/what-causes-the-error-java-security-invalidkeyexception-parameters-missing
public static String encrypt(byte[] key, String value) throws GeneralSecurityException {
    // Argument validation.
    if (key.length != 16) {
        throw new IllegalArgumentException("Invalid key size.");
    }

    // Setup AES tool.
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));

    // Do the job with AES tool.
    byte[] original = value.getBytes(Charset.forName("UTF-8"));
    byte[] binary = cipher.doFinal(original);
    return Base64.encodeToString(binary, Base64.DEFAULT);
}

// // http://stackoverflow.com/questions/17322002/what-causes-the-error-java-security-invalidkeyexception-parameters-missing
public static String decrypt(byte[] key, String encrypted) throws GeneralSecurityException {
    // Argument validation.
    if (key.length != 16) {
        throw new IllegalArgumentException("Invalid key size.");
    }

    // Setup AES tool.
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));

    // Do the job with AES tool.
    byte[] binary = Base64.decode(encrypted, Base64.DEFAULT);
    byte[] original = cipher.doFinal(binary);
    return new String(original, Charset.forName("UTF-8"));
}
