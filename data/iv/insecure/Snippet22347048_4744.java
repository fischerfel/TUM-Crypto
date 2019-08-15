public static String decrypt(String key, byte[] encrypted)
            throws GeneralSecurityException {

        byte[] raw = key.getBytes(Charset.forName("US-ASCII"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec,new IvParameterSpec(new byte[16]));
        byte[] original = cipher.doFinal(encrypted);

        return new String(original, Charset.forName("US-ASCII"));
    }
