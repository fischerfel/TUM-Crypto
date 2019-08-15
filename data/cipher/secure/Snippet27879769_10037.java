    // Get the salt
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[saltLength];
    random.nextBytes(salt);

    // Secret key
    SecretKey secretKey = getSecretKey(seed, salt);

    // Get Cipher instance for AES with Padding algorithm PKCS5
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    // Initialization vector, as CBC requires IV
    byte[] iv = new byte[cipher.getBlockSize()];
    random.nextBytes(iv);

    // Algorithm spec for IV
    IvParameterSpec ivParams = new IvParameterSpec(iv);

    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);

    // Encrypt the text
    byte[] encryptedTextInBytes = cipher.doFinal(textToBeEncrypted
            .getBytes("UTF-8"));

    return Base64Encoder.encode(encryptedTextInBytes);
