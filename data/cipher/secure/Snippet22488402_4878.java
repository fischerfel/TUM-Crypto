static byte[] encryptEnv(Envelope message, SecretKey secretKey) {
    try {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] serializedEnv = serializeEnv(message);
        return cipher.doFinal(serializedEnv);
    } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace(System.err);
    } catch (IllegalBlockSizeException | BadPaddingException e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace(System.err);
    }
    return null;
}

static Envelope decryptEnv(int length, byte[] bytes, SecretKey secretKey, IvParameterSpec ivSpec) {
    try {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decrypted = cipher.doFinal(bytes);
        return deserializeEnv(decrypted);
    } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace(System.err);
    } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace(System.err);
    }
    return null;
}
