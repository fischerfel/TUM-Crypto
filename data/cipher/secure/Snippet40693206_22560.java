private byte[] decrypt(byte[] data, String corporateId, String algorithm)
        throws Exception {
    String path = corporateId + ".key";

    byte[] key = (byte[]) null;
    try {
        key = returnbyte(path);
    } catch (IOException e) {
        e.printStackTrace();
    }
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    this.logger.info("Provider Info " + cipher.getProvider().getInfo());
    byte[] keyBytes = new byte[16];

    int len = key.length;
    if (len > keyBytes.length) {
        len = keyBytes.length;
    }
    System.arraycopy(key, 0, keyBytes, 0, len);
    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

    IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
    cipher.init(2, keySpec, ivSpec);
    BASE64Decoder decoder = new BASE64Decoder();

    byte[] results = decoder.decodeBuffer(hexStringFromBytes(data));

    byte[] ciphertext = cipher.doFinal(results);

    return ciphertext;
}
