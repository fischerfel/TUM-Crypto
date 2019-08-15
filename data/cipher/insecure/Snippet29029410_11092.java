public class Main {



//output file name after decryption
private static String decryptedFileName;
//input encrypted file
private static String fileSource;
//a prefix tag for output file name
private static String outputFilePrefix = "decrypted_";
//My key for decryption, its the same as in the encrypter program.
static byte[] key = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6 };

//Decrypting function
public static void decrypt(byte[] key, File inputFile, File outputFile) throws Exception {
    try {

        Key secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int) inputFile.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        FileOutputStream outputStream = new FileOutputStream(outputFile, true);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

//first argument is the intput file source
public static void main(String[] args) {

    if (args.length != 1) {
        System.out.println("Add log file name as a parameter.");

    } else {
        fileSource = args[0];

        try {
            File sourceFile = new File(fileSource);
            if (sourceFile.exists()) {

                //Decrption
                decryptedFileName = outputFilePrefix + sourceFile.getName();
                File decryptedFile = new File(decryptedFileName);
                decrypt(key, sourceFile, decryptedFile);
            } else {
                System.out.println("Log file not found: " + fileSource);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Decryption done, output file: " + decryptedFileName);
    }

}

}
