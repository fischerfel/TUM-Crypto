public class Cryptographer {
private static final String ALGORITHM = "AES";

private final static String ALGO_RANDOM_NUM_GENERATOR = "SHA1PRNG";

public static int encrypt(String key, File inputFile, File outputFile)
        throws CryptographyException {
    if(!outputFile.getParentFile().exists())outputFile.getParentFile().mkdir();
    return StartCryptography(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
}

public static int decrypt(String key, File inputFile, File outputFile)
        throws CryptographyException {
    if(!outputFile.getParentFile().exists())outputFile.getParentFile().mkdir();
    return StartCryptography(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
}

private static int StartCryptography(int cipherMode, String key, File inputFile,
                                     File outputFile) throws CryptographyException {
    int performance = -1;
    try {
        SecureRandom random = SecureRandom.getInstance(ALGO_RANDOM_NUM_GENERATOR);
        random.setSeed(key.getBytes());
        KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
        generator.init(random);
        SecretKey key1 = generator.generateKey();

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(cipherMode, key1);

        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int) inputFile.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();
        performance = 1;

    } catch (NoSuchPaddingException | NoSuchAlgorithmException
            | InvalidKeyException | BadPaddingException
            | IllegalBlockSizeException | IOException ex) {
        throw new CryptographyException("Error encrypting/decrypting file", ex);
    } finally {
        return performance;
    }

 }
}
