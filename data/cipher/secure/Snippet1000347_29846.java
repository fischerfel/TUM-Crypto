public static byte[] encrypt(byte[] encrptdByte) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    byte[] encryptionByte = null;
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    encryptionByte = cipher.doFinal(encrptdByte);
    return encryptionByte;
}

public static void decrypt(byte[] encrptdByte) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
    byte[] encryptionByte = null;
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, publicKey);
    encryptionByte = cipher.doFinal(encrptdByte);

    System.out.println("Recovered String     :::  " + new String(encryptionByte));
}
