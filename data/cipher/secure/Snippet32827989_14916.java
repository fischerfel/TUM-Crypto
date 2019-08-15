File encryptedFile = getFileTodecrypt();
    byte[] salt = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
    SecretKeyFactory factory =     SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), salt, 1000, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

    /* Encrypt the message. */
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);

    AlgorithmParameters params = cipher.getParameters();
    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

    File fileToDecrypt = getFileTodecrypt(); // this gets my file
    InputStream inputStream = FileUtils.openInputStream(fileToDecrypt);
    byte[] bytesToBeDecrypted = IOUtils.toByteArray(inputStream);

    // file bytes does not work
    //byte[] ciphertext = cipher.doFinal(bytesToBeDecrypted);
    // file to string does not work
    //byte[] ciphertext = cipher.doFinal(encryptedString.getBytes("UTF-8"));
    // this works, but does not take c# code into account at all
    byte[] ciphertext = cipher.doFinal("hello".getBytes("UTF-8"));

    IvParameterSpec iv2 = new IvParameterSpec(iv);

    //decrypt
    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
    String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
    System.out.println(plaintext);

}

private String convertFileToString(File encryptedFile) throws IOException {
    String fileString = FileUtils.readFileToString(encryptedFile, "UTF-8");
    return fileString;

}
