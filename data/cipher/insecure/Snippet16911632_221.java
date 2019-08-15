public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException {

    String encryptedText = user.read(fileNameOK);
    String key1 = user.read(fileName1);
    String key2 = user.read(fileName2);
    String encryptedSeanceKey1 = user.read(fileNameEK1);
    String encryptedSeanceKey2 = user.read(fileNameEK2);




    SecretKey secretKey1=getKeyInstance(key1);
    SecretKey secretKey2=getKeyInstance(key2);



    Cipher aesCipher = Cipher.getInstance("AES");
    aesCipher.init(Cipher.DECRYPT_MODE,secretKey1,aesCipher.getParameters());




    //byte[] byteKey = encryptedSeanceKey1.getBytes();

       byte[] byteDecryptedKey = aesCipher.doFinal(encryptedSeanceKey1.getBytes());
       String decryptedKey1 = new String(byteDecryptedKey);
       System.out.println("Расшифрованный сеансовый ключ с помощью секретного ключа 1: " +decryptedKey1);

    aesCipher.init(Cipher.DECRYPT_MODE,secretKey2,aesCipher.getParameters());




    byte[] byteKey2 = encryptedSeanceKey2.getBytes();
        byteDecryptedKey = aesCipher.doFinal(byteKey2); 
        String decryptedKey2 = new String(byteDecryptedKey);
       System.out.println("Расшифрованный сеансовый ключ с помощью секретного ключа 2: " +decryptedKey2);





        // Расшифрование данных
        aesCipher.init(Cipher.DECRYPT_MODE,getKeyInstance(decryptedKey1),aesCipher.getParameters());

         byte[] byteText = encryptedText.getBytes();

        byte[] byteDecryptedText = aesCipher.doFinal(byteText);
        decryptedText = new String(byteDecryptedText);
        System.out.println(" Расшифрованный текст " +decryptedText);



}
