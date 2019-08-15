//Arbitrarily selected 8-byte salt sequence:
private static final byte[] salt = {
    (byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,
    (byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17 
};

public static Cipher makeCipher(String pass, Boolean decryptMode) throws GeneralSecurityException{

    //Use a KeyFactory to derive the corresponding key from the passphrase:
    PBEKeySpec keySpec = new PBEKeySpec(pass.toCharArray());
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");//PBKDF2WithHmacSHA256
    SecretKey key = keyFactory.generateSecret(keySpec);

    //Create parameters from the salt and an arbitrary number of iterations:
    PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 42);

    //Set up the cipher:
    Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

    //Set the cipher mode to decryption or encryption:
    if(decryptMode){
        cipher.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);
    } else {
        cipher.init(Cipher.DECRYPT_MODE, key, pbeParamSpec);
    }

    return cipher;
}


/**Encrypts one file to a second file using a key derived from a passphrase:**/
public static void encryptFile(String fileName, String pass){
    try{
        byte[] decData;
        byte[] encData;
        File inFile = new File(fileName);
        //Generate the cipher using pass:
        Cipher cipher = Main.makeCipher(pass, true);

        //Read in the file:
        FileInputStream inStream = new FileInputStream(inFile);

        int blockSize = 8;
        //Figure out how many bytes are padded
        int paddedCount = blockSize - ((int)inFile.length()  % blockSize );

        //Figure out full size including padding
        int padded = (int)inFile.length() + paddedCount;

        decData = new byte[padded];


        inStream.read(decData);

        inStream.close();

        //Write out padding bytes as per PKCS5 algorithm
        for( int i = (int)inFile.length(); i < padded; ++i ) {
            decData[i] = (byte)paddedCount;
        }

        //Encrypt the file data:
        encData = cipher.doFinal(decData);

        writeToFile(fileName, encData);

    } catch(Exception e){
        e.printStackTrace();
    }
}

private static void writeToFile(String path, byte[] data) {
    try {
        File file = new File(path);

        //Write the encrypted data to a new file:
        FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(data);
        outStream.close();
    } catch(Exception e){
        e.printStackTrace();
    }
}
