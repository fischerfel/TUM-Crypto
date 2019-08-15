public static String decryptionWithFile(String encrypted,String privateFile2)throws Exception  {
    PrivateKey privateKey = getPrivateKey(privateFile2);

    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] bts = Hex.decodeHex(encrypted.toCharArray());
    bts = cipher.doFinal(bts);


    bts = getFinalBytesOfDycryptedString(bts);
    String decryptedMessage = new String(cipher.doFinal(encrypted.getBytes()));
    return new String(bts,"UTF-8");
}
