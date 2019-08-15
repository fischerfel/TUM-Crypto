  String strDecriptedValue = decrypt(passkey, responseBase64);

public static String decrypt(String seed, String encrypted)
            throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] enc = Base64.decode(encrypted.getBytes(), Base64.DEFAULT);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(seed);
        keygen.init(128, random); 
        SecretKey key = keygen.generateKey();
        byte[] raw = key.getEncoded();
        return raw;
    }

private static byte[] decrypt(byte[] raw, byte[] encrypted)
            throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
