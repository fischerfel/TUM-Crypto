public byte[] rsaDecrypt(byte[] sampleText,String pbkeypath) {
    PublicKey pubKey = null;
    try {
        pubKey = readKeyFromFile(pbkeypath);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("RSA");
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    try {
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    byte[] cipherData = null;
    try {
        cipherData = cipher.doFinal(sampleText);
        // cipherData = cipher.
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return cipherData;
}
