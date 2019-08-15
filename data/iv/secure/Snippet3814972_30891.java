public AESObfuscator(byte[] salt, String applicationId, String deviceId) {
        Log.w("AESObfuscator", "constructor starting");
        try {
            Log.w("AESObfuscator", "1");
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEYGEN_ALGORITHM);
            Log.w("AESObfuscator", "2");
            KeySpec keySpec =
                new PBEKeySpec((applicationId + deviceId).toCharArray(), salt, 1024, 256);
            Log.w("AESObfuscator", "3");
            SecretKey tmp = factory.generateSecret(keySpec);
            Log.w("AESObfuscator", "4");
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
            Log.w("AESObfuscator", "5");
            mEncryptor = Cipher.getInstance(CIPHER_ALGORITHM);
            Log.w("AESObfuscator", "6");
            mEncryptor.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(IV));
            Log.w("AESObfuscator", "7");
            mDecryptor = Cipher.getInstance(CIPHER_ALGORITHM);
            Log.w("AESObfuscator", "8");
            mDecryptor.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(IV));
        } catch (GeneralSecurityException e) {
            // This can't happen on a compatible Android device.
            throw new RuntimeException("Invalid environment", e);
        }
        Log.w("AESObfuscator", "constructor done");
    }
