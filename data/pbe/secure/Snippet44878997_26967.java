public static String encrypt(String str) {
    try {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(str.toCharArray(), salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret); //<--- Need to do this before writing IvPerameterSpec,
        // But I think that it's not possible if I have it in another method.
        byte[] encryptedText = cipher.doFinal(str.getBytes("UTF-8"));

        return new String(encryptedText);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
