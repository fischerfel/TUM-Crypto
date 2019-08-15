public static void main(String[] args) throws Exception{

    outFile_enc = new FileOutputStream(mFileNameEncrypted);
    outFile_dec = new FileOutputStream(mFileNameDecrypted);

    int keyLength = 256;
    // salt
    salt = new byte[16];
    Random rnd = new Random();
    rnd.nextBytes(salt);
    int iterations = 4000;

    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec keySpec = new PBEKeySpec(mPassWord.toCharArray(), salt, iterations, keyLength);
    SecretKey passwordKey = keyFactory.generateSecret(keySpec);
    key = new SecretKeySpec(passwordKey.getEncoded(), "AES");

    // creates a cipher and init it for encryption
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key);

    AlgorithmParameters params = cipher.getParameters();
    iv = params.getParameterSpec(IvParameterSpec.class).getIV();

    encryptData(cipher);            
}

public static void encryptData(Cipher cipher) throws Exception{
    // File to encrypt
    inFile = new FileInputStream(mFileName);

    // unique random salt in the first 16 bytes of the file
    outFile_enc.write(salt);

    // Read file and encrypt its bytes
    byte[] input  = new byte[64];
    int bytesRead;
    while((bytesRead = inFile.read(input)) != -1){
    byte[] output = cipher.update(input, 0, bytesRead);
    if(output != null)
        outFile_enc.write(output);
    }

    byte[] output = cipher.doFinal();
    if(output != null)
        outFile_enc.write(output);
    // random initialization vector is stored at the end of a page
    outFile_enc.write(iv);

    inFile.close();
    outFile_enc.flush();
    outFile_enc.close();    
}
