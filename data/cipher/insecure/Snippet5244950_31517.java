          // create a key generator based upon the Blowfish cipher
    KeyGenerator keygenerator = KeyGenerator.getInstance("Blowfish");

    // create a key
    SecretKey secretkey = keygenerator.generateKey();

    // create a cipher based upon Blowfish
    Cipher cipher = Cipher.getInstance("Blowfish");

    // initialise cipher to with secret key
    cipher.init(Cipher.ENCRYPT_MODE, secretkey);

    // get the text to encrypt
    String inputText = "MyTextToEncrypt";

    // encrypt message
    byte[] encrypted = cipher.doFinal(inputText.getBytes());
