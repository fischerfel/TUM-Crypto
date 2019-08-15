 public String kryptaa (String[] args) throws Exception {
    //
    // Check args and get plaintext
    if (args.length !=1) {
        System.err.println("Usage: java PrivateExample text");
        System.exit(1);
    }
    byte[] plainText = args[0].getBytes("UTF8");
    //
    // Get a DES private key
    KeyGenerator keyGen = KeyGenerator.getInstance("DES");

    // If you do not initialize the KeyGenerator, each provider supply a default initialization.
    keyGen.init(56);
    Key key = keyGen.generateKey();
    //
    // Creates the DES Cipher object (specifying the algorithm, mode, and padding).
    Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    // Print the provider information       
    //
    // Initializes the Cipher object.
    cipher.init(Cipher.ENCRYPT_MODE, key);
    // Encrypt the plaintext using the public key
    byte[] cipherText = cipher.doFinal(plainText);
    String kryptattu = Base64.getEncoder().encodeToString(cipherText);
    String erikryptattu = ( new String(cipherText, "UTF8") );
    return kryptattu;
}
