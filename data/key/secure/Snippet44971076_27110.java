public class TestFileEncryption {
    private static void mainCrypto(int cipherMode, File inputFile, File outputFile) throws Exception{
        //Let the user enter the key they wish to use
        Key secretKey = new SecretKeySpec(UITest.getStoreKey().getBytes(), UITest.getSendAlg()); //Generates a key based on the default keysize for the specified algorithm

        //Generate an Initialization Vector (IV)
        final int ALG_KEYLENGTH = UITest.getStoreKey().length(); //Change this as desired for the security level you want
        byte[] iv = new byte[ALG_KEYLENGTH]; //Save the IV bytes or send it in plaintext with the encrypted data so you can decrypt the data later
        SecureRandom prng = new SecureRandom(); //Use SecureRandom to generate random bits. The size of the IV matches the blocksize of the cipher
        prng.nextBytes(iv); //Construct the appropriate IvParameterSpec object for the data to pass to Cipher's init() method

        //Create a Cipher by specifying the following parameters: Alg name, Mode (CBC), Padding (PKC7/PKCS5)
        Cipher cipherForEncryption = Cipher.getInstance(UITest.getSendAlg() + "/CBC/PKCS5PADDING"); // Must specify the mode explicitly as most JCE providers default to ECB mode

        //Initialize the Cipher for Encryption
        cipherForEncryption.init(cipherMode, secretKey, new IvParameterSpec(iv));

        //Declare / Initialize the Data, Convert the Input to Bytes and encrypt or decrypt using doFinal.
        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int) inputFile.length() - ALG_KEYLENGTH];
        inputStream.read(iv);
        inputStream.read(inputBytes);
        byte[] outputBytes = cipherForEncryption.doFinal(inputBytes);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(iv);
        outputStream.write(outputBytes);
        inputStream.close();
        outputStream.close();
    }

    public static void encrypt(File inputFile, File outputFile) throws Exception {
        mainCrypto(Cipher.ENCRYPT_MODE, inputFile, outputFile); //ENC_MODE = Constant used to initialize cipher to encryption mode.
    }

    public static void decrypt(File inputFile, File outputFile) throws Exception {
        mainCrypto(Cipher.DECRYPT_MODE, inputFile, outputFile); //ENC_MODE = Constant used to initialize cipher to encryption mode.
    }

    public static void main(String[] args) {}
}
