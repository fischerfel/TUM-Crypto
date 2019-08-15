    SecureRandom rnd = new SecureRandom();
    byte[] iv = new byte[16];
    rnd.nextBytes(iv);

    String password = "password";
    byte[] plaintext = "plaintext".getBytes(StandardCharsets.UTF_8);

    IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
    PBEParameterSpec pbeParamSpec = new PBEParameterSpec(SALT, 10000, ivParamSpec);
    PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
    try {
        SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
        SecretKey secretKey = kf.generateSecret(keySpec);

        // On J2SE the SecretKeyfactory does not actually generate a key, it just wraps the password.
        // The real encryption key is generated later on-the-fly when initializing the cipher
        System.out.println(new String(secretKey.getEncoded()));

        // Encrypt
        Cipher enc = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
        enc.init(Cipher.ENCRYPT_MODE, secretKey, pbeParamSpec);
        byte[] encrypted = enc.doFinal(plaintext);
        System.out.println("Encrypted text: " + new BASE64Encoder().encode(encrypted));

        // Decrypt
        Cipher dec = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
        dec.init(Cipher.DECRYPT_MODE, secretKey, pbeParamSpec);
        byte[] decrypted = dec.doFinal(encrypted);
        String message = new String(decrypted, StandardCharsets.UTF_8);

        System.out.println(message);

    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
        e.printStackTrace();
    }
