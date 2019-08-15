        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(keyString.toCharArray(), SALT, 256, 128);
        byte[] encoded = factory.generateSecret(spec).getEncoded(); 
        assert encoded.length*8 == 128;
        Key rv = new SecretKeySpec(encoded, "AES");
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.DECRYPT_MODE, rv, new IvParameterSpec(IV_PARAMETER_SPEC));
