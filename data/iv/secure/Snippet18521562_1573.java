 public static String encrypt(String in) throws Exception {
    String out = " ";
    // generate a key
    KeyGenerator keygen = KeyGenerator.getInstance("DES");
    keygen.init(56);
    byte[] key = keygen.generateKey().getEncoded();
    SecretKeySpec skeySpec = new SecretKeySpec(key, "DES");

    // build the initialization vector
    SecureRandom random = new SecureRandom();
    byte iv[] = new byte[8]; //generate random 8 byte IV. 
    random.nextBytes(iv);
    IvParameterSpec ivspec = new IvParameterSpec(iv);
    // initialize the cipher for encrypt mode
    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);

    byte[] encrypted = cipher.doFinal(in.getBytes());

    out = asHex(encrypted);

    return out;
}
