 public String tulkkaa (String[] args) throws Exception {
    //
    // Check args and get plaintext
    if (args.length !=1) {
        System.err.println("Usage: java PrivateExample text");
        System.exit(1);
    }
    byte[] krypted = args[0].getBytes("UTF8");
    //
    // Get a DES private key
    KeyGenerator keyGen = KeyGenerator.getInstance("DES");

    // If you do not initialize the KeyGenerator, each provider supply a default initialization.
    keyGen.init(56);
    Key key = keyGen.generateKey();
    //
    // Creates the DES Cipher object (specifying the algorithm, mode, and padding).
    Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    // Initializes the Cipher object.
    cipher.init(Cipher.DECRYPT_MODE, key);
    // Decrypt the ciphertext using the same key
    byte[] newPlainText = cipher.doFinal(krypted);
