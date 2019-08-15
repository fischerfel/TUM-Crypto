public static String decrypt(String in) throws NoSuchAlgorithmException,
  NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
  IllegalBlockSizeException, BadPaddingException, IOException, KeyFileNotFoundException, UnknownKeyException {

    String out = " ";

    byte[] key = readKey("key").clone(); //<--from file
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

    byte[] iv = readKey("iv"); //<-- from file
    IvParameterSpec ivspec = new IvParameterSpec(iv);

    //initialize the cipher for decryption
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);

    // decrypt the message
    byte[] decrypted = cipher.doFinal(in.getBytes());

    out = asHex(decrypted);

    return out;
}
