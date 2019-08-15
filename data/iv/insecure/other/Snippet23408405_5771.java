private byte[] encrypt(byte[] plainText) throws Exception {
    byte[] ivBytes = new byte[16];
    new Random().nextBytes(ivBytes);

    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    Cipher cipher = Cipher.getInstance("AES/" + encryptionMode + "/PKCS5Padding", "BC");
    SecretKeySpec keyy = new SecretKeySpec(key, "AES");

    // in next line Java throws exception
    cipher.init(Cipher.ENCRYPT_MODE, keyy, ivSpec);

    byte[] cryptograph = cipher.doFinal(plainText);
    return ArrayUtils.addAll(ivBytes, cryptograph);
}
