public static final String ALGORITHM = "RSA";
public static byte[] encrypt(String text, PublicKey key) {
    byte[] cipherText = null;
    try {
        // get an RSA cipher object and print the provider
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        // encrypt the plain text using the public key
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(text.getBytes());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return cipherText;
}
public static String decrypt(byte[] text, PrivateKey key) {
    byte[] dectyptedText = null;
    try {
        // SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // get an RSA cipher object and print the provider
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        // random.nextBytes(text);
        // decrypt the text using the private key
        cipher.init(Cipher.DECRYPT_MODE, key);
        dectyptedText = cipher.doFinal(text);

    } catch (Exception ex) {
        ex.printStackTrace();
    }

    return new String(dectyptedText);
}
