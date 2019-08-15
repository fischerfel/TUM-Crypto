        public static String generateDecryptedKey(String secretKey, String authKey)
    {
        String strDecryptedKey = "";
        byte[] salt = { (byte)0x09, (byte)0xD5, (byte)0xA1, (byte)0xA6, (byte)0xA3, (byte)0xA7, (byte)0xA9, (byte)0xA0 };
        int iterationCount = 10;
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        dcipher = Cipher.getInstance(key.getAlgorithm());
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        byte[] enc = Base64.decodeBase64(authKey.getBytes());
        byte[] utf8 = dcipher.doFinal(enc);
        strDecryptedKey = new String(utf8, "UTF-8");
        return strDecryptedKey;
    }
