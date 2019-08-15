private KeyStore mKeyStore;

private static final String KEY_ALIAS = "MyKey";
void testEncryption() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException, IOException, CertificateException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, UnrecoverableEntryException, NoSuchPaddingException {

    mKeyStore = KeyStore.getInstance("AndroidKeyStore");
    mKeyStore.load(null);

    // Generate Key Pair -------------------------------------
    KeyPairGenerator kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
    kpg.initialize(new KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
            .setKeySize(2048)
            .build());
    KeyPair kp = kpg.generateKeyPair();

    // Encrypt -----------------------------------------------
    KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)mKeyStore.getEntry(KEY_ALIAS, null);
    PublicKey publicKey = (PublicKey) privateKeyEntry.getCertificate().getPublicKey();
    Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    String x = "It doesn't have to be perfect, it's just for demonstration.";

    byte [] vals = cipher.doFinal(x.getBytes("UTF-8"));

    byte[] encryptedBytes = Base64.encode(vals, Base64.DEFAULT);
    String encryptedText = new String(encryptedBytes, "UTF-8");


    // Decrypt -----------------------------------------------
    PrivateKey privateKey = privateKeyEntry.getPrivateKey();

    Cipher output = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
    output.init(Cipher.DECRYPT_MODE, privateKey/*, spec */);

    byte[] bxx = Base64.decode(encryptedText, Base64.DEFAULT);
    byte[] bytes = output.doFinal(bxx);  // <= throws IllegalBlocksizeException

    String finalText = new String(bytes, 0, bytes.length, "UTF-8");
}
