public static String encode(Key publicKey, String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    byte[] byteData = data.getBytes(); // convert string to byte array

    Cipher cipher = Cipher.getInstance(ALGORITHM); // create conversion processing object
    cipher.init(Cipher.ENCRYPT_MODE, publicKey); // initialize object's mode and key

    byte[] encryptedByteData = cipher.doFinal(byteData); // use object for encryption

    return new String(encryptedByteData); // convert encrypted byte array to string and return it

}

public static String decode(Key privateKey, String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    byte[] byteData = data.getBytes(); // convert string to byte array

    Cipher cipher = Cipher.getInstance(ALGORITHM); // create conversion processing object
    cipher.init(Cipher.DECRYPT_MODE, privateKey); // initialize object's mode and key

    System.out.println(byteData.length);

    byte[] decryptedByteData = cipher.doFinal(byteData); // use object for decryption

    return new String(decryptedByteData); // convert decrypted byte array to string and return it

}
