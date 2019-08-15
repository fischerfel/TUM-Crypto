public String decrypte(final String encCardNo) throws IllegalBlockSizeException,
        BadPaddingException {
    // --- setup (should be stored differently)
    final char[] keystpassw = "9801461740".toCharArray();
    final String alias = "ksjksformat";

    // --- retrieve private key from store
    final PrivateKey key;
    try (final FileInputStream is = new FileInputStream(
            "C:/Users/admin/Desktop/keystore/ksjksformat.jks")) {
        final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(is, keystpassw);
        key = (PrivateKey) ks.getKey(alias, keystpassw);
    } catch (final KeyStoreException | IOException | NoSuchAlgorithmException | UnrecoverableKeyException | CertificateException e) {
        throw new IllegalStateException("Could not load key from key store", e);
    }

    // --- initialize cipher
    final Cipher cipher;
    try {
        // should use OAEP instead
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
    } catch (final NoSuchAlgorithmException | NoSuchPaddingException e) {
        throw new IllegalStateException(
                "RSA PKCS v1.5 should always be available", e);
    } catch (final InvalidKeyException e) {
        throw new IllegalStateException("Key is not an RSA private key", e);
    }

    // --- decode 
    final byte[] decoded;
    try {
        decoded = Base64.getDecoder().decode(encCardNo);
    } catch (final IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid encoded ciphertext", e);
    }

    // --- decrypt
    final byte[] cipherData = cipher.doFinal(decoded);
    final String cardNo = new String(cipherData, StandardCharsets.US_ASCII);

    // --- clean up
    try {
        key.destroy();
    } catch (final DestroyFailedException e) {
        // we tried, possibly log this
    }
    Arrays.fill(cipherData, (byte) 0);

    return cardNo;
}
