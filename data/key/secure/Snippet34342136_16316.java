private static final String ALGORIT = "AES";

public static String encryptHackro(String plaintext, String key)
throws NoSuchAlgorithmException, NoSuchPaddingException,
InvalidKeyException, IllegalBlockSizeException,
BadPaddingException, IOException, DecoderException {


    byte[] raw = DatatypeConverter.parseHexBinary(key);

    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    Cipher cipher = Cipher.getInstance(ALGORITMO);
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

    byte[] cipherText = cipher.doFinal(plaintext.getBytes(""));
    byte[] iv = cipher.getIV();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    outputStream.write(iv);
    outputStream.write(cipherText);

    byte[] finalData = outputStream.toByteArray();

    String encodedFinalData = DatatypeConverter.printBase64Binary(finalData);

    return encodedFinalData;

}
