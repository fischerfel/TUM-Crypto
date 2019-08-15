private static void createKey(char[] password) throws Exception {
    System.out.println("Generating a Blowfish key...");

    // Create a blowfish key
    KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
    keyGenerator.init(256);
    Key key = keyGenerator.generateKey();

    System.out.println("Done generating the key.");

    // Now we need to create the file with the key,
    // encrypting it with a password.
    byte[] salt = new byte[8];
    SecureRandom random = new SecureRandom();
    random.nextBytes(salt);
    PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHAAndTwofish-CBC");
    SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
    PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, ITERATIONS);

    //Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES-CBC");
    //Cipher cipher = Cipher.getInstance("PBEWithSHAAndTwofish-CBC");
    Cipher cipher = Cipher.getInstance("PBEWithSHAAndTwofish-CBC");

    cipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

    // Encrypt the key
    byte[] encryptedKeyBytes = cipher.doFinal(key.getEncoded());

    // Write out the salt, and then the encrypted key bytes
    FileOutputStream fos = new FileOutputStream(KEY_FILENAME);
    fos.write(salt);
    fos.write(encryptedKeyBytes);
    fos.close();
}
