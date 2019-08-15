private static PrivateKey privateKey = null;
private static PublicKey publicKey = null;

public static void generateKeyPair() {
    KeyPairGenerator keyPairGenerator = null;
    KeyPair keyPair = null;

    try {
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

    keyPairGenerator.initialize(1024);
    keyPair = keyPairGenerator.generateKeyPair();

    RSA.privateKey = keyPair.getPrivate();
    RSA.publicKey = keyPair.getPublic();
}

public static String getEncodedPublicKey() {
    return (new String(Base64.encode(RSA.getPublicKey().getEncoded())));
}

public static String encrypt(String string) {
    byte[] encrypted = null;

    try {
        Cipher cipher = Cipher.getInstance("RSA");//174 bytes

        cipher.init(Cipher.ENCRYPT_MODE, RSA.getPublicKey());
        encrypted = cipher.doFinal(string.getBytes());
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    }

    return (new String(Base64.encode(encrypted)));
}

public static String decrypt(String string) {
    byte[] decrypted = null;

    try {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        cipher.init(Cipher.DECRYPT_MODE, RSA.getPrivateKey());
        decrypted = cipher.doFinal(Base64.decode(string));
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (Base64DecodingException e) {
        e.printStackTrace();
    }

    return (new String(decrypted));
}
