public class Encryptor {

private static final String ALGORITHM = "AES";
private static final String TRANSFORMATION = "AES";

public void encrypt(String key, File inputFile, File outputFile) throws CryptoException {
    doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
}

public void decrypt(String key, File inputFile, File outputFile) throws CryptoException {
    doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
}    
private void doCrypto(int cipherMode, String key, File inputFile, File outputFile) throws CryptoException {
    try {

        Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(cipherMode, secretKey);

        byte[] inputBytes = new byte[16];
        byte[] outputBytes = new byte[16];

        //Open the file in read write mode
        RandomAccessFile fileStore = new RandomAccessFile(inputFile, "rw"); 
        fileStore.seek(0); 

        //encrypt first 1024bytes
        int bytesRead = 0;
        for(int ctr=0;bytesRead!= -1 && ctr<64 ;ctr++){
            //get file pointer position
            long prevPosition = fileStore.getFilePointer();

            //read 16 bytes to array
            bytesRead = fileStore.read(inputBytes); 

            //if successful, go move back pointer and overwrite these 16 bytes with encrypted bytes
            if(bytesRead != 1){
                outputBytes = cipher.doFinal(inputBytes);
                fileStore.seek(prevPosition);
                fileStore.write(outputBytes);
            }   
        }

        fileStore.close();

    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
            | IllegalBlockSizeException | IOException ex) {
        throw new CryptoException(ex);
    }
}
