public byte[] encrypt(byte[] plainText, SecretKey secretKey, String outputFilePath) throws Exception
{
    //select putput file for encrypted text
    FileOutputStream fos = new FileOutputStream(outputFilePath);

    //Cipher in encrypt mode
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

    fos.write(cipher.doFinal(plainText));
    fos.close();

    //Encrypted text is returned in a byte array.
    return cipher.doFinal(plainText);
}

// Decryption function
public String decrypt(String sharedKeyFilepath, String cipherTextFilepath, String plainTextFilepath) throws Exception
{
    FileInputStream fis = null;


    File sharedKeyFile = new File(sharedKeyFilepath);
    byte[] sharedKeyByte = new byte[(int)sharedKeyFile.length()];

    File cipherTextFile = new File(cipherTextFilepath);
    byte[] cipherText = new byte[(int)cipherTextFile.length()];

    File plainTextFile = new File(plainTextFilepath);
    byte[] plainText = new byte[(int)plainTextFile.length()];

    fis = new FileInputStream(sharedKeyFile);
    fis.read(sharedKeyByte);
    fis.close();

    fis = new FileInputStream(cipherTextFile);
    fis.read(cipherText);
    fis.close();

    fis = new FileInputStream(plainTextFile);
    fis.read(plainText);
    fis.close();

    SecretKey sharedKey = new SecretKeySpec(sharedKeyByte, 0, sharedKeyByte.length, "AES");

    // Cipher in decrypt mode
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, sharedKey, new IvParameterSpec(iv));

    // Select output file for decrypted cipher Text
    FileOutputStream fos = new FileOutputStream("Receiver/DecryptedPlainText.txt");
    String decrypted = new String(cipher.doFinal(cipherText));

    byte [] decryptedBytes = cipher.doFinal(cipherText);
    fos.write(decryptedBytes);
    fos.close();

    // Return the decrypted text as a string.
    return decrypted;
}
