    InputStream is = new FileInputStream(...); //Input stream

    SecretKeySpec skey = new SecretKeySpec(Hex.decodeHex(key.toCharArray()), "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skey); 
    FileOutputStream fileOuputStream = new FileOutputStream(SD_CARD_PATH+ "/" + "abcd.db"); 
    CipherOutputStream cos = new CipherOutputStream(fileOuputStream, cipher);

    //Now read from input and write to output using your favorite utilities library
    //Guava and Apache Commons IO are good examples.
    FooUtils.copy(is, cos);
    //Remember to close streams if the previous call didn't (preferably in a finally block)
