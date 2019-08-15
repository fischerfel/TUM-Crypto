public byte[] AES_Encrypt(byte[] bytesToBeEncrypted, byte[] passwordBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
{
    byte[] encryptedBytes = null;
    byte[] saltBytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
    final Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
    final byte[] keyData = Arrays.copyOf(passwordBytes, KEY_SIZE
            / Byte.SIZE);
    final byte[] ivBytes = Arrays.copyOf(keyData, cipher.getBlockSize());
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyData, "AES"),
            new IvParameterSpec(ivBytes));
    encryptedBytes = cipher.doFinal(bytesToBeEncrypted);
    return encryptedBytes;
}
