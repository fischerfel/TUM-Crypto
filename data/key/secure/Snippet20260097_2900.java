public static byte[] iv = new SecureRandom().generateSeed(16);

public static void main(String[] args) {
    String plainText = "Look mah, I'm a message!";
    System.out.println("Original plaintext message: " + plainText);

    // Initialize two key pairs
    KeyPair keyPairA = generateECKeys();
    KeyPair keyPairB = generateECKeys();

    // Create two AES secret keys to encrypt/decrypt the message
    SecretKey secretKeyA = generateSharedSecret(keyPairA.getPrivate(),
            keyPairB.getPublic());
    System.out.println(bytesToHex(secretKeyA.getEncoded()));
    SecretKey secretKeyB = generateSharedSecret(keyPairB.getPrivate(),
            keyPairA.getPublic());
    System.out.println(bytesToHex(secretKeyB.getEncoded()));

    // Encrypt the message using 'secretKeyA'
    String cipherText = encryptString(secretKeyA, plainText);
    System.out.println("Encrypted cipher text: " + cipherText);

    // Decrypt the message using 'secretKeyB'
    String decryptedPlainText = decryptString(secretKeyB, cipherText);
    System.out.println("Decrypted cipher text: " + decryptedPlainText);
}

public static KeyPair generateECKeys() {
    try {
        ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec("secp521r1");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                "ECDH", "BC");

        keyPairGenerator.initialize(parameterSpec);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        System.out.println("Private key length: "
                + keyPair.getPrivate().getEncoded().length);
        System.out.println("Public key length: "
                + keyPair.getPublic().getEncoded().length);
        return keyPair;
    } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
            | NoSuchProviderException e) {
        e.printStackTrace();
        return null;
    }
}

public static SecretKey generateSharedSecret(PrivateKey privateKey,
        PublicKey publicKey) {
    try {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH", "BC");
        keyAgreement.init(privateKey);
        keyAgreement.doPhase(publicKey, true);

        SecretKey key = keyAgreement.generateSecret("AES");
        System.out.println("Shared key length: " + key.getEncoded().length);
        return key;
    } catch (InvalidKeyException | NoSuchAlgorithmException
            | NoSuchProviderException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return null;
    }
}

public static String encryptString(SecretKey key, String plainText) {
    try {
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        byte[] plainTextBytes = plainText.getBytes("UTF-8");
        byte[] cipherText;

        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        cipherText = new byte[cipher.getOutputSize(plainTextBytes.length)];
        int encryptLength = cipher.update(plainTextBytes, 0,
                plainTextBytes.length, cipherText, 0);
        encryptLength += cipher.doFinal(cipherText, encryptLength);

        return bytesToHex(cipherText);
    } catch (NoSuchAlgorithmException | NoSuchProviderException
            | NoSuchPaddingException | InvalidKeyException
            | InvalidAlgorithmParameterException
            | UnsupportedEncodingException | ShortBufferException
            | IllegalBlockSizeException | BadPaddingException e) {
        e.printStackTrace();
        return null;
    }
}

public static String decryptString(SecretKey key, String cipherText) {
    try {
        Key decryptionKey = new SecretKeySpec(key.getEncoded(),
                key.getAlgorithm());
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        byte[] cipherTextBytes = hexToBytes(cipherText);
        byte[] plainText;

        cipher.init(Cipher.DECRYPT_MODE, decryptionKey, ivSpec);
        plainText = new byte[cipher.getOutputSize(cipherTextBytes.length)];
        int decryptLength = cipher.update(cipherTextBytes, 0,
                cipherTextBytes.length, plainText, 0);
        decryptLength += cipher.doFinal(plainText, decryptLength);

        return new String(plainText, "UTF-8");
    } catch (NoSuchAlgorithmException | NoSuchProviderException
            | NoSuchPaddingException | InvalidKeyException
            | InvalidAlgorithmParameterException
            | IllegalBlockSizeException | BadPaddingException
            | ShortBufferException | UnsupportedEncodingException e) {
        e.printStackTrace();
        return null;
    }
}

public static String bytesToHex(byte[] data, int length) {
    String digits = "0123456789ABCDEF";
    StringBuffer buffer = new StringBuffer();

    for (int i = 0; i != length; i++) {
        int v = data[i] & 0xff;

        buffer.append(digits.charAt(v >> 4));
        buffer.append(digits.charAt(v & 0xf));
    }

    return buffer.toString();
}

public static String bytesToHex(byte[] data) {
    return bytesToHex(data, data.length);
}

public static byte[] hexToBytes(String string) {
    int length = string.length();
    byte[] data = new byte[length / 2];
    for (int i = 0; i < length; i += 2) {
        data[i / 2] = (byte) ((Character.digit(string.charAt(i), 16) << 4) + Character
                .digit(string.charAt(i + 1), 16));
    }
    return data;
}
