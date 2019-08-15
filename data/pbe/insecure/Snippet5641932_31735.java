public String dec(String password, String salt, String encString)
        throws Exception {

    byte[] ivData = toByte(encString.substring(0, 32));
    byte[] encData = toByte(encString.substring(32));

    // get raw key from password and salt
    PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(),
            toByte(salt), 50, 256);
    SecretKeyFactory keyFactory = SecretKeyFactory
            .getInstance("PBEWithSHA256And256BitAES-CBC-BC");
    SecretKeySpec secretKey = new SecretKeySpec(keyFactory.generateSecret(
            pbeKeySpec).getEncoded(), "AES");
    byte[] key = secretKey.getEncoded();

    // setup cipher parameters with key and IV
    KeyParameter keyParam = new KeyParameter(key);
    CipherParameters params = new ParametersWithIV(keyParam, ivData);

    // setup AES cipher in CBC mode with PKCS7 padding
    BlockCipherPadding padding = new PKCS7Padding();
    BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
            new CBCBlockCipher(new AESEngine()), padding);
    cipher.reset();
    cipher.init(false, params);

    // create a temporary buffer to decode into (it'll include padding)
    byte[] buf = new byte[cipher.getOutputSize(encData.length)];
    int len = cipher.processBytes(encData, 0, encData.length, buf, 0);
    len += cipher.doFinal(buf, len);

    // remove padding
    byte[] out = new byte[len];
    System.arraycopy(buf, 0, out, 0, len);

    // return string representation of decoded bytes
    return new String(out, "UTF-8");
}
