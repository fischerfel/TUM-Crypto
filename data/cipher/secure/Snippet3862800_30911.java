private byte[] aesEncryptedInfo(String info) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, InvalidAlgorithmParameterException, NoSuchProviderException {
    Security.addProvider(new BouncyCastleProvider());
    SecretKey secret = new SecretKeySpec(CUSTOMLONGSECRETKEY.substring(0, 32).getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
    cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(VECTOR_SECRET_KEY.getBytes()));
    return cipher.doFinal(info.getBytes("UTF-8"));
}
