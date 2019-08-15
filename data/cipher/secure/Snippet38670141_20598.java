SecureRandom sr= new SecureRandom(cipher_key.getBytes());
      KeyGenerator kg=KeyGenerator.getInstance("twofish");
      kg.init(sr);
      SecretKey sk = kg.generateKey();
      // create an instance of cipher
        Cipher cipher = Cipher.getInstance("twofish");

        // initialize the cipher with the key
        cipher.init(Cipher.ENCRYPT_MODE, sk);

        // enctypt!
        encrypted = cipher.doFinal(word.getBytes());
