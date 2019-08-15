public void InitCiphers()
            throws NoSuchAlgorithmException,
            NoSuchProviderException,
            NoSuchProviderException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException{

   //1. create the cipher using Bouncy Castle Provider
   encryptCipher =
           Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
   //2. create the key
   SecretKey keyValue = new SecretKeySpec(key,"AES");
   //3. create the IV
   AlgorithmParameterSpec IVspec = new IvParameterSpec(IV);
   //4. init the cipher
   //encryptCipher.init(Cipher.ENCRYPT_MODE, keyValue, IVspec);
   encryptCipher.init(Cipher.ENCRYPT_MODE, keyValue);

   //1 create the cipher
   decryptCipher =
           Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
   //2. the key is already created
   //3. the IV is already created
   //4. init the cipher
   //decryptCipher.init(Cipher.DECRYPT_MODE, keyValue, IVspec);
   decryptCipher.init(Cipher.DECRYPT_MODE, keyValue);
}

public void CBCEncryptCipherStream(InputStream fis, OutputStream fos) throws IOException, ShortBufferException, IllegalBlockSizeException, BadPaddingException{
    byte[] buf = new byte[1024];

    fos = new CipherOutputStream(fos, encryptCipher);

    int numRead = 0;
    while ((numRead = fis.read(buf)) >= 0) {
        fos.write(buf, 0, numRead);
    }
    fos.close();
}

public InputStream CBCDecryptStream(InputStream encryptedInputStream){
    InputStream decryptedInputStream = null;
    decryptedInputStream = new CipherInputStream(encryptedInputStream, decryptCipher);
    return decryptedInputStream;

}
