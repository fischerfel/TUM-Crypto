public static byte[] getSafeKey(String key, byte[] initVector) {
    KeySpec keySpec = new PBEKeySpec(key.toCharArray(), initVector,
            ITERATION_COUNT, KEY_LENGTH);
    SecretKeyFactory keyFactory = null;
    try {
        keyFactory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    byte[] keyBytes = new byte[0];
    try {
        keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    }
    SecretKey skey = new SecretKeySpec(keyBytes, "AES");

    return skey.getEncoded();
}
