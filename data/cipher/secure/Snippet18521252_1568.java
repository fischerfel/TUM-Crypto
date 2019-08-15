public static String encrypt(String in) throws NoSuchAlgorithmException,
   NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
   IllegalBlockSizeException, BadPaddingException, IOException {

    String out = " ";

    // generate a key
    KeyGenerator keygen = KeyGenerator.getInstance("AES");
    keygen.init(128);
    byte[] key = keygen.generateKey().getEncoded();
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

    // build the initialization vector
    SecureRandom random = new SecureRandom();
    byte iv[] = new byte[16]; //generate random 16 byte IV. AES is always 16bytes
    random.nextBytes(iv);
    IvParameterSpec ivspec = new IvParameterSpec(iv);

    saveKey(key, iv); //<-- save to file

    // initialize the cipher for encrypt mode
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);

    byte[] encrypted = cipher.doFinal(in.getBytes());

    out = asHex(encrypted);

    return out;
}
