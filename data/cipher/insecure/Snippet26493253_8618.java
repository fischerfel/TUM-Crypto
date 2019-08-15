public static byte[] decrypt(final byte[] value, final String key) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
    final DESKeySpec objDesKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
    final SecretKeyFactory objKeyFactory = SecretKeyFactory.getInstance("DES");
    final SecretKey objSecretKey = objKeyFactory.generateSecret(objDesKeySpec);
    final byte[] rgbIV = key.getBytes();
    final IvParameterSpec iv = new IvParameterSpec(rgbIV);
    final Cipher objCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    objCipher.init(2, objSecretKey, iv);
    return objCipher.doFinal(value);
}
