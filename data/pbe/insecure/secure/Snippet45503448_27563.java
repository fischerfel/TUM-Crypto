public class FileEncryptor {    
    private static String filename;
    private static String password;
    private static FileInputStream inFile;
    private static FileOutputStream outFile;
    public static String tempFilename;
    public static File tempFile;

    public static File encryptFile(File f, String passkey) throws Exception {
        if(f.isDirectory()) {
            JOptionPane.showMessageDialog(null, "file object is a directory");
            return null;
        }
        filename = f.getPath();
        password = passkey;     
        //Need to create a temporary file which is filled with the encrypted data.
        tempFilename = filename + ".des";
        tempFile = new File(tempFilename);      
        inFile = new FileInputStream(f);
        outFile = new FileOutputStream(tempFile);       
        // Use PBEKeySpec to create a key based on a password.
        // The password is passed as a character array.
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory sKeyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey sKey = sKeyFac.generateSecret(keySpec);       
        byte[] salt = new byte[8];
        Random rnd = new Random();
        rnd.nextBytes(salt);
        int iterations = 100;       
         //Create the parameter spec for this salt and iteration count
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);
        //Create the cipher and initiate it for encryption
        Cipher c = Cipher.getInstance("PBEWithMD5AndDES");
        c.init(Cipher.ENCRYPT_MODE, sKey, parameterSpec);

        //Need to write the salt into the file. It is required for decryption
        outFile.write(salt);

        //Read the file and encrypt its bytes
        byte[] input = new byte[64];
        int bytesRead;
        while((bytesRead = inFile.read(input)) != -1) {
            byte[] output = c.update(input, 0, bytesRead);
            if(output != null) { outFile.write(output); }           
        }

        byte[] output = c.doFinal();
        if(output != null) { outFile.write(output); }

        //Closing the streams before exiting.
        inFile.close();
        outFile.flush();
        outFile.close();

        return tempFile;
    }

}
