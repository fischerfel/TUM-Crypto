public static String decrypt(String in) throws NoSuchAlgorithmException,
  NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
  IllegalBlockSizeException, BadPaddingException, IOException,
  KeyFileNotFoundException, UnknownKeyException {

    String out = " "; //decrypted String to return

    byte[] key = readKey("key").clone(); //my attempt to copy a byte array
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

    byte iv[] = readKey("iv"); //works here? same as above so I don't know.
    IvParameterSpec ivspec = new IvParameterSpec(iv);

    //initialize the cipher for decryption
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);

    // decrypt the message
    byte[] decrypted = cipher.doFinal(in.getBytes());

    out = asHex(decrypted);

    return out;
}
