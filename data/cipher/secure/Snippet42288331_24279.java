public static String generateSymmetricKey() {

    KeyGenerator keyGenerator;
    try {
        keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey key = keyGenerator.generateKey();

        Cipher encodeCipher = Cipher.getInstance("AES/CTR/NoPadding");
        encodeCipher.init(Cipher.ENCRYPT_MODE, key);

        String bigSecret = Base64.getEncoder().encodeToString(
                key.getEncoded());

        return bigSecret;

    } catch (Exception e) {
        System.err.println("Secret Key generation failed: "
                + e.getMessage());
        throw new RuntimeException(e);
    }

}
