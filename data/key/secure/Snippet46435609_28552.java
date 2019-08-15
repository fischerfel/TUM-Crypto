static MessageDigest MD5 = null;

static {
    try {
        MD5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}


public static String decrypt(String cipherText, String password)
        throws GeneralSecurityException, UnsupportedEncodingException {
    byte[] passwordBytes = hexStringToByteArray(password);
    byte[] keyBytes = MD5.digest(passwordBytes);
    byte[] keyAndPassword = new byte[keyBytes.length + passwordBytes.length];
    System.arraycopy(keyBytes, 0, keyAndPassword, 0, keyBytes.length);
    System.arraycopy(passwordBytes, 0, keyAndPassword, keyBytes.length, passwordBytes.length);
    byte[] ivBytes = MD5.digest(keyAndPassword);
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec iv = new IvParameterSpec(ivBytes);

    byte[] encrypted = hexStringToByteArray(cipherText);
    Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
    aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
    byte[] decryptedData = aesCBC.doFinal(encrypted);
    return new String(decryptedData, StandardCharsets.UTF_8);
}

public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
}
