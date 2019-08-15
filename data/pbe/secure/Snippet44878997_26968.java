public static String decrypt(String str) {
    try {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(str.toCharArray(), salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        //                                                ^^^ Returns NullPointerException

        byte[] ciphertext = cipher.doFinal(str.getBytes("UTF-8"));
        String decryptedText = new String(cipher.doFinal(ciphertext), "UTF-8");

        return new String(decryptedText);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
