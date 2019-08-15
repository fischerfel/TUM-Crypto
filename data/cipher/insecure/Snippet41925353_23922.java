private void encryptKeysFile() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, IOException{
    SecretKey key64 = new SecretKeySpec( new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 }, "Blowfish" );
    Cipher cipher = Cipher.getInstance( "Blowfish" );
    cipher.init( Cipher.ENCRYPT_MODE, key64 );
    File keysFile = new File(System.getProperty("src"),fileName);
    SealedObject sealedObject = new SealedObject(keysFile, cipher);
    CipherOutputStream cipherOutputStream = new CipherOutputStream( new BufferedOutputStream( new FileOutputStream(fileName) ), cipher );
    ObjectOutputStream outputStream = new ObjectOutputStream( cipherOutputStream );
    outputStream.writeObject(sealedObject);
    outputStream.close();
}
