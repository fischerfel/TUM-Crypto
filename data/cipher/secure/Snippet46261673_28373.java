private char[] hashTheKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
    messageDigest.update(key.getBytes());
    return Base64.encodeToString(messageDigest.digest(),
                                 Base64.NO_PADDING).toCharArray();
}

private SecretKey getSecretKey(char[] key) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException {
    return new SecretKeySpec(
        SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        .generateSecret(new PBEKeySpec(key,
                       "ABCD".getBytes("UTF8"),
                       100, 128)).getEncoded(), "AES");
}

public String decrypt(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeySpecException {
    byte[] dataBytes = Base64.decode(data, Base64.DEFAULT);
    SecretKey secretKey = getSecretKey(hashTheKey("ABCD"));
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(2, secretKey, new IvParameterSpec(new byte[16]),
            SecureRandom.getInstance("SHA1PRNG"));
    return new String(cipher.doFinal(dataBytes));
}
