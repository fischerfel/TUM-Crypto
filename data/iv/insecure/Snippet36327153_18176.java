public void demoSymmetricEncryption() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {

    String keyAlgorithm = "DiffieHellman";
    String keyAgreementAlgorithm = "DiffieHellman";
    String keySpecAlgorithm = "AES";
    String cipherAlgorithm = "AES/CBC/PKCS5Padding";

    KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(keyAlgorithm);
    keyGenerator.initialize(1024, new SecureRandom());
    KeyPair tomKeyPair = keyGenerator.generateKeyPair();
    PrivateKey tomPrivateKey = tomKeyPair.getPrivate();
    PublicKey tomPublicKey = tomKeyPair.getPublic();

    KeyPair steveKeyPair = keyGenerator.generateKeyPair();
    PrivateKey stevePrivateKey = steveKeyPair.getPrivate();
    PublicKey stevePublicKey = steveKeyPair.getPublic();

    int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
    System.out.println("Limited encryption policy files installed : " + (maxKeyLen == 128)); // returns false

    KeyAgreement tomKeyAgreement = KeyAgreement.getInstance(keyAgreementAlgorithm);
    keyGenerator.initialize(1024, new SecureRandom());
    tomKeyAgreement.init(tomPrivateKey);
    tomKeyAgreement.doPhase(stevePublicKey, true);
    byte[] tomSecret = tomKeyAgreement.generateSecret();

    SecretKeySpec tomSecretKeySpec = new SecretKeySpec(tomSecret, keySpecAlgorithm);

    KeyAgreement steveKeyAgreement = KeyAgreement.getInstance(keyAgreementAlgorithm);
    steveKeyAgreement.init(stevePrivateKey);
    steveKeyAgreement.doPhase(tomPublicKey, true);
    byte[] steveSecret = steveKeyAgreement.generateSecret();

    SecretKeySpec steveSecretKeySpec = new SecretKeySpec(steveSecret, keySpecAlgorithm);

    System.out.println("Secret Keys are identical : " + steveSecretKeySpec.equals(tomSecretKeySpec)); // returns true

    String initVector = "RandomInitVector";

    Cipher encryptCipher = Cipher.getInstance(cipherAlgorithm);
    IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));

    // fails because AES key is 128 bytes not 128 bits in length - think I need to use KDF hash to shrink it appropriately.
    encryptCipher.init(Cipher.ENCRYPT_MODE, tomSecretKeySpec, iv);


    // Attempt to use the cipher

    byte[] encryptedData = encryptCipher.doFinal("Hello".getBytes());

    Cipher decryptCipher = Cipher.getInstance(cipherAlgorithm);
    iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
    decryptCipher.init(Cipher.DECRYPT_MODE, steveSecretKeySpec, iv);

    byte[] decryptedData = decryptCipher.doFinal(encryptedData);

    System.out.println("Decrypted Data : " + new String(decryptedData));

}
