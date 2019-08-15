public static byte[] decryptWithPem(String alg, String pemAlg, byte[] encryptedData, Path pemPath) {
    try {
        Cipher cipher = Cipher.getInstance(alg, "BC");
        cipher.init(Cipher.DECRYPT_MODE, loadPrivateKey(pemPath, pemAlg));
        return cipher.doFinal(encryptedData);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

private static PrivateKey loadPrivateKey(Path keyPath, String alg) {
    try {
        byte[] keyData = Util.base64DecodeAsBytes(IOUtil.fileToString(keyPath).replaceAll("\\s", ""));
        KeyFactory keyFactory = KeyFactory.getInstance(alg);
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyData);
        return keyFactory.generatePrivate(privateKeySpec);
    } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
        throw new RuntimeException(e);
    }
}

private static SecretKeySpec getSecretKeySpec(String alg, byte[] key) {
    return new SecretKeySpec(key, alg);
}

public static byte[] decrypt(String alg, String keyAlg, byte[] dataToDecrypt, byte[] key) {
    try {
        Cipher cipher = Cipher.getInstance(alg, "BC");
        cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(keyAlg, key), new IvParameterSpec(dataToDecrypt, 0, 16));
        return cipher.doFinal(Arrays.copyOfRange(dataToDecrypt, 16, dataToDecrypt.length));
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
