public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CFB8/NoPadding";

    Charset CHARSET = Charset.forName("UTF8");

    public static void encrypt(byte[] key, byte[] iv, File inputFile, File outputFile) throws CryptoException {
        doCrypto(Cipher.ENCRYPT_MODE, key, iv, inputFile, outputFile);
    }

    public static void decrypt(byte[] key, byte[] iv, File inputFile, File outputFile) throws CryptoException {
        doCrypto(Cipher.DECRYPT_MODE, key, iv, inputFile, outputFile);
    }

    private static void doCrypto(int cipherMode, byte[] key, byte[] iv, File inputFile, File outputFile)
            throws CryptoException {
        try {
            Key secretKey = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey, ivSpec);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            throw new CryptoException("Error encrypting/decrypting file", e);
        }
    }
}
