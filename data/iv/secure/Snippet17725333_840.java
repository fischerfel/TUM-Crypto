public static byte[] encrypt(byte[] decrypted, byte[] keyvalue) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
    MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
    byte[] key = sha256.digest(keyvalue);

    MessageDigest md5 = MessageDigest.getInstance("MD5");
    byte[] checksum = md5.digest(decrypted);

    //The length of the value to encrypt must be a multiple of 16.
    byte[] decryptedAndChecksum = new byte[(decrypted.length + md5.getDigestLength() + 15) / 16 * 16];
    System.arraycopy(decrypted, 0, decryptedAndChecksum, 0, decrypted.length);
    System.arraycopy(checksum, 0, decryptedAndChecksum, decrypted.length, checksum.length);
    //The remaining bytes of decryptedAndChecksum stay as 0 (default byte value) because PHP pads with 0's.

    SecureRandom rnd = new SecureRandom();
    byte[] iv = new byte[16];
    rnd.nextBytes(iv);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);

    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), ivSpec);
    byte[] encrypted = Base64.encodeBase64(cipher.doFinal(decryptedAndChecksum));

    byte[] ivBase64 = Base64.encodeBase64String(iv).substring(0, 22).getBytes();
    byte[] output = new byte[encrypted.length + ivBase64.length];
    System.arraycopy(ivBase64, 0, output, 0, ivBase64.length);
    System.arraycopy(encrypted, 0, output, ivBase64.length, encrypted.length);
    return output;
}
