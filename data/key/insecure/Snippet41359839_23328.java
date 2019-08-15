static String key = "xxx"; // key should be exactly 16bit long
private static final String ALGORITHM = "AES";
private static final String TRANSFORMATION = "AES";

public static void encrypt(File inputFile, File outputFile) throws CryptoException {
    doCrypto(Cipher.ENCRYPT_MODE, inputFile, outputFile);
}
public static void decrypt(File inputFile, File outputFile) throws CryptoException {
    doCrypto(Cipher.DECRYPT_MODE, inputFile, outputFile);
}

private static void doCrypto(int cipherMode, File inputFile, File outputFile) throws CryptoException {
    try {
        Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(cipherMode, secretKey);

        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int) inputFile.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();
    } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
        e.printStackTrace();
        throw new CryptoException("Error encrypting/decrypting file", e);
    }
}
