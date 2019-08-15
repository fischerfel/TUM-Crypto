public static void Decrypt(String fileIn, String fileOut, byte[] key, byte[] IV, long offset)  
{
// First we are going to open the file streams 
FileInputStream fsIn;
try 
{
    fsIn = new FileInputStream(fileIn);,,
    FileOutputStream fsOut = new FileOutputStream(fileOut);

    // create cipher object
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(IV));

    // create the encryption stream
    CipherInputStream cis = new CipherInputStream(fsIn, cipher);

    // set a buffer and keep writing to the stream
    int bufferLen = KiloByte;
    byte[] buffer = new byte[bufferLen];
    int bytesRead = 0;

    // read a chunk of data from the input file
    while ( (bytesRead = cis.read(buffer, 0, bufferLen)) != -1)
    {
        // write to file  
        fsOut.write(buffer, 0, bytesRead);
    } 

    fsOut.flush();
    // close streams 
    fsOut.close();
    cis.close();
} catch (FileNotFoundException e) {
    e.printStackTrace();
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
} catch (NoSuchPaddingException e) {
    e.printStackTrace();
} catch (InvalidKeyException e) {
    e.printStackTrace();
} catch (InvalidAlgorithmParameterException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}
}
