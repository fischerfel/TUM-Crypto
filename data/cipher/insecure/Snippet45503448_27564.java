public class FileDecryptor {
 
    private static String filename;
    private static String password;
    private static FileInputStream inFile;
    private static FileOutputStream outFile;

    public static File decryptFile(File encryptedFile, String passkey) throws NoSuchAlgorithmException, 
                                                            InvalidKeySpecException, IOException, NoSuchPaddingException,
                                                            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        String encryptedfilename = encryptedFile.getPath();
        password = passkey;

        inFile = new FileInputStream(encryptedFile);
        StringBuffer sb = new StringBuffer(encryptedfilename);
        sb.reverse();
        sb.delete(0, 3);
        sb.reverse();           //removing the ".des" extension of the encrypted file
        filename = new String(sb) + ".dec";

        File decrypFile = new File(filename);
        outFile = new FileOutputStream(decrypFile);

        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory sKeyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey sKey = sKeyFac.generateSecret(keySpec);

        // Read in the previously stored salt and set the iteration count.
        byte[] salt = new byte[8];
        inFile.read(salt);
        int iterations = 100;

        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);

        //Create the cipher and initialize it for decryption.
        Cipher c = Cipher.getInstance("PBEWithMD5AndDES");
        c.init(Cipher.DECRYPT_MODE, sKey, parameterSpec);

        byte[] input = new byte[64];
        int bytesRead;
        while((bytesRead = inFile.read(input)) != -1) {
            byte[] output = c.update(input, 0, bytesRead);
            if(output != null) {
                outFile.write(output);
            }
        }

        byte[] output = c.doFinal();
        System.out.println("Decrypted the data....");
        System.out.println("Wrting the data into file!!");
        if(output != null) {
            outFile.write(output);
        }
        System.out.println("Closing the streams");
        inFile.close();
        outFile.flush();
        outFile.close();

        return decrypFile;      
    }
}
