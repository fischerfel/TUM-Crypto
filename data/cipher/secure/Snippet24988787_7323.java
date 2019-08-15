public String RSAEncrypt(final String plain, PublicKey publicKey ) throws NoSuchAlgorithmException, NoSuchPaddingException,
    InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte [] encryptedBytes = cipher.doFinal(plain.getBytes());
    String encrypted = bytesToString(encryptedBytes);
    System.out.println("EEncrypted?????" + encrypted );
    return encrypted;
}

public String RSADecrypt(String encryptedBytes,PrivateKey privateKey ) throws NoSuchAlgorithmException, NoSuchPaddingException,
    InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
    Cipher cipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher1.init(Cipher.DECRYPT_MODE, privateKey);
    byte [] decryptedBytes = cipher1.doFinal(stringToBytes(encryptedBytes));

    String decrypted = new String(decryptedBytes);
    System.out.println("DDecrypted?????" + decrypted);
    return decrypted;
    }

public  String bytesToString(byte[] b) {
    byte[] b2 = new byte[b.length + 1];
    b2[0] = 1;
    System.arraycopy(b, 0, b2, 1, b.length);
    return new BigInteger(b2).toString(36);
}

public  byte[] stringToBytes(String s) {
    byte[] b2 = new BigInteger(s, 36).toByteArray();
    return Arrays.copyOfRange(b2, 1, b2.length);
}
