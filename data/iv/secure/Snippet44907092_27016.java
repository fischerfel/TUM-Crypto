public class encwork {
private static String keyString = "ykHySDZCWr16TVku"; //Encryption key
private static void bulkWork(int cipherMode, File inputFile, File outputFile) throws Exception{
    //Let the user enter the key they wish to use
    Key secretKey = new SecretKeySpec(keyString.getBytes(), "AES"); //Generates a key based on the default keysize for the specified algorithm

    //Generate an Initialization Vector (IV)
    final int ALG_KEYLENGTH = 128; //Change this as desired for the security level you want
    byte[] iv = new byte[ALG_KEYLENGTH / 8]; //Save the IV bytes or send it in plaintext with the encrypted data so you can decrypt the data later
    SecureRandom prng = new SecureRandom(); //Use SecureRandom to generate random bits. The size of the IV matches the blocksize of the cipher
    prng.nextBytes(iv); //Construct the appropriate IvParameterSpec object for the data to pass to Cipher's init() method

    //Create a Cipher by specifying the following parameters: Alg name, Mode (CBC), Padding (PKC7/PKCS5)
    Cipher cipherForEncryption = Cipher.getInstance("AES/CBC/PKCS5PADDING"); // Must specify the mode explicitly as most JCE providers default to ECB mode

    //Initialize the Cipher for Encryption
    cipherForEncryption.init(cipherMode, secretKey, new IvParameterSpec(iv));

    //Declare / Initialize the Data, Convert the Input to Bytes and encrypt or decrypt using doFinal.
    FileInputStream inputStream = new FileInputStream(inputFile);
    byte[] inputBytes = new byte[(int) inputFile.length()];
    inputStream.read(inputBytes);
    byte[] outputBytes = cipherForEncryption.doFinal(inputBytes);
    FileOutputStream outputStream = new FileOutputStream(outputFile);
    outputStream.write(outputBytes);
    inputStream.close();
    outputStream.close();
}

public static void main(String[] args) {
    File inputFile = new File("C:/Users/admin/Desktop/Crypto/In/test.txt"); 
    File encryptedFile = new File("C:/Users/admin/Desktop/Crypto/Enc/test.encrypted");
    File decryptedFile = new File("C:/Users/admin/Desktop/Crypto/Dec/testdec.txt");

    //Encryption
    try {
        encwork.encrypt(inputFile, encryptedFile); //Encrypt method
    } catch (Exception e) {
        e.printStackTrace(); //Will show what caused the error in the console if an error occurs
    }

    //Decryption
    try {
        encwork.decrypt(encryptedFile, decryptedFile); //Decrypt method
    } catch (Exception e) {
        e.printStackTrace(); //Will show what caused the error in the console if an error occurs
    }
}

public static void encrypt(File inputFile, File outputFile) throws Exception {
    bulkWork(Cipher.ENCRYPT_MODE, inputFile, outputFile); //ENC_MODE = Constant used to initialize cipher to encryption mode.
}

public static void decrypt(File inputFile, File outputFile) throws Exception {
    bulkWork(Cipher.DECRYPT_MODE, inputFile, outputFile); //ENC_MODE = Constant used to initialize cipher to encryption mode.
}}
