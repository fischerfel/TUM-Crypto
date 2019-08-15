private void startEncryption() throws UnsupportedEncodingException {
    String text = "15";
    String key = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    String ivString = "LXUJOLKDEJKGTMAV";
    byte[] valueBytes = text.getBytes();
    byte[] keyBytes = keyString.getBytes("UTF-8");
    byte[] ivBytes = ivString.getBytes();
    byte[] ivFinalBytes = new byte[16];
    for(int i=0; i<16; i++){
        ivFinalBytes[i] = ivBytes[i];
    }
    encrypt(valueBytes, keyBytes, ivFinalBytes);
}

private void encrypt(byte[] valueBytes, byte[] keyBytes, byte[] ivBytes) {
    try {
        SecretKeySpec ks = new SecretKeySpec(keyBytes, "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
        c.init(Cipher.ENCRYPT_MODE, ks, ivParameterSpec);
        byte[] clearText = c.doFinal(valueBytes);
        String ivPass = Base64.encodeToString(c.getIV(), Base64.DEFAULT);
        String pass = Base64.encodeToString(clearText, Base64.DEFAULT);
        encryptedPassword = ivPass+pass;
        encryptedPassword = encryptedPassword.replace("\n", "").replace("\r", "");
    } catch (Exception e) {
        System.out.println("exception: "+e);
    }
}
