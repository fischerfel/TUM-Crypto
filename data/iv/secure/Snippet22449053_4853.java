   // Create a Blowfish key
    KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");

    // Now set the keysize to 256 bits
    keyGenerator.init(256);

    Key key = keyGenerator.generateKey();

    System.out.println("Done generating the key.");

    // Create a cipher using that key to initialize it
    Cipher cipher = Cipher.getInstance("Blowfish/CFB8/NoPadding");


        //Cipher encrypter = Cipher.getInstance("Blowfish/C/NoPadding");
    System.out.println("good here");
     SecureRandom random = new SecureRandom();
    byte[] iv = new byte[16];
    random.nextBytes(iv);


  IvParameterSpec spec = new IvParameterSpec(iv);

    cipher.init(Cipher.ENCRYPT_MODE, key,spec);
