String strDataToEncrypt = new String();
String strCipherText = new String();
String strDecryptedText = new String();

try {
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(128);
    SecretKey secretKey = keyGen.generateKey();

    final int AES_KEYLENGTH = 128;  
    byte[] iv = new byte[AES_KEYLENGTH / 8];    
    SecureRandom prng = new SecureRandom();
    prng.nextBytes(iv);

    Cipher aesCipherForEncryption = Cipher.getInstance("AES/CBC/PKCS7PADDING");

    aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

    strDataToEncrypt = "Hello World of Encryption using AES ";
    byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
    byte[] byteCipherText = aesCipherForEncryption.doFinal(byteDataToEncrypt);

    strCipherText = new BASE64Encoder().encode(byteCipherText);
    System.out.println("Cipher Text generated using AES is " + strCipherText);


    Cipher aesCipherForDecryption = Cipher.getInstance("AES/CBC/PKCS7PADDING"); 

    aesCipherForDecryption.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
    byte[] byteDecryptedText = aesCipherForDecryption.doFinal(byteCipherText);
    strDecryptedText = new String(byteDecryptedText);

    System.out.println(" Decrypted Text message is " + strDecryptedText);

} catch (NoSuchAlgorithmException noSuchAlgo) {
    System.out.println(" No Such Algorithm exists " + noSuchAlgo);
} catch (NoSuchPaddingException noSuchPad) {
    System.out.println(" No Such Padding exists " + noSuchPad);
} catch (InvalidKeyException invalidKey) {
    System.out.println(" Invalid Key " + invalidKey);
} catch (BadPaddingException badPadding) {
    System.out.println(" Bad Padding " + badPadding);
} catch (IllegalBlockSizeException illegalBlockSize) {
    System.out.println(" Illegal Block Size " + illegalBlockSize);
} catch (InvalidAlgorithmParameterException invalidParam) {
    System.out.println(" Invalid Parameter " + invalidParam);
}
