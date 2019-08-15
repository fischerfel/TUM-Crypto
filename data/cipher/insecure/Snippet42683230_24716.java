    // Generate double length 3DES Master Key
    KeyGenerator masterEncKeyGenerator = KeyGenerator.getInstance("DESede");
    masterEncKeyGenerator.init(112);
    SecretKey masterKey = masterEncKeyGenerator.generateKey();

    //Prepare random bytes
    byte[] randomKeyValue = "rn4yrbdy".getBytes();

    // Encrypt random bytes with the 3DES Master key
    final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, masterKey);
    byte[] encryptedRandomValue = cipher.doFinal(randomKeyValue);

    // Derive new key 3DES Key
    SecretKeyFactory mySecretKeyFactory = SecretKeyFactory.getInstance("DESede");
    DESedeKeySpec myKeySpec = new DESedeKeySpec(encryptedRandomValue);
    SecretKey derivedKey = mySecretKeyFactory.generateSecret(myKeySpec);
