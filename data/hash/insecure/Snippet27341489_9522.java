    String          input = "input message test";
    MessageDigest   hash = MessageDigest.getInstance("SHA1");

    hash.update(Utils.toByteArray(input));

    System.out.println("digest : " +hash.digest());

    Cipher           cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    SecureRandom     random = new SecureRandom();

    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

    generator.initialize(512, random);

    KeyPair          pair = generator.generateKeyPair();
    Key              pubKey = pair.getPublic();
    Key              privKey = pair.getPrivate();

    cipher.init(Cipher.ENCRYPT_MODE, privKey);

    byte[] ciphertext = cipher.doFinal(hash.digest());

    System.out.println("cipher: " + ciphertext);

    cipher.init(Cipher.DECRYPT_MODE, pubKey);

    byte[] plaintext = cipher.doFinal(ciphertext);

    System.out.println("plaintext : " + plaintext);
