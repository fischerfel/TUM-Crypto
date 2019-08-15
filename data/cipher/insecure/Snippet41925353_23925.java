private File dencryptKeysFile() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, IOException, ClassNotFoundException, BadPaddingException{
    SecretKey key64 = new SecretKeySpec( new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 }, "Blowfish" );
    Cipher cipher = Cipher.getInstance( "Blowfish" );
    cipher.init( Cipher.DECRYPT_MODE, key64 );
    CipherInputStream cipherInputStream = new CipherInputStream(new BufferedInputStream(new FileInputStream(fileName)),cipher);
    ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
    SealedObject sealedObject = (SealedObject)inputStream.readObject();
    inputStream.close();

    File keysFile =(File)sealedObject.getObject(cipher);
    this.keysFile = keysFile;
    return keysFile;
}
