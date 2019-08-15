try{
    String plainData="my name is laksahan",cipherText,decryptedText;
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(128);
    SecretKey secretKey = keyGen.generateKey();
    Cipher aesCipher = Cipher.getInstance("AES");
    aesCipher.init(Cipher.ENCRYPT_MODE,secretKey);
    byte[] byteDataToEncrypt = plainData.getBytes();
    byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt);
    cipherText = new BASE64Encoder().encode(byteCipherText);
System.out.println(cipherText);
}catch(Exception e){

}
