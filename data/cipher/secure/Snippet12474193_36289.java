public byte[] RSAEncrypt(final String plain) throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(1024);
    kp = kpg.genKeyPair();
    publicKey = kp.getPublic();
    privateKey = kp.getPrivate();

    cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    encryptedBytes = cipher.doFinal(plain.getBytes());
    System.out.println("EEncrypted?????" + org.apache.commons.codec.binary.Hex.encodeHexString(encryptedBytes));
    return encryptedBytes;
}

public String RSADecrypt(final byte[] encryptedBytes) throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    cipher1 = Cipher.getInstance("RSA");
    cipher1.init(Cipher.DECRYPT_MODE, privateKey);
    decryptedBytes = cipher1.doFinal(encryptedBytes);
    decrypted = new String(decryptedBytes);
    System.out.println("DDecrypted?????" + decrypted);
    return decrypted;
}
