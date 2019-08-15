public static void encrypt(Path inputFile, OutputStream output) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
    // init cipher
    KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
    SecretKey myDesKey = keygenerator.generateKey();
    Cipher desalgCipher;
    desalgCipher = Cipher.getInstance("DES");
    desalgCipher.init(Cipher.ENCRYPT_MODE, myDesKey);


    try(InputStream is = Files.newInputStream(inputFile);        // get an IS on your file
    CipherInputStream cipherIS = new CipherInputStream(is, desalgCipher)){   // wraps input Stream with cipher
        copyStreams(cipherIS, output);  // copyStream is let to the implementer's choice.
    }
}
