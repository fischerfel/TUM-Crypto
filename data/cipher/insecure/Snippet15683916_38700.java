    //Read your key
    FileInputStream keyFis = new FileInputStream("key.txt");
    byte[] encKey = new byte[keyFis.available()];
    keyFis.read(encKey);
    keyFis.close();
    Key keyFromFile = new SecretKeySpec(encKey, "DES");
    //Read your text
    FileInputStream encryptedTextFis = new FileInputStream("test.txt");
    byte[] encText = new byte[encryptedTextFis.available()];
    encryptedTextFis.read(encText);
    encryptedTextFis.close();
    //Decrypt
    Cipher decrypter = Cipher.getInstance("DES/ECB/PKCS5Padding");
    decrypter.init(Cipher.DECRYPT_MODE, keyFromFile);
    byte[] decryptedText = decrypter.doFinal(encText);
    //Print result
    System.out.println("Decrypted Text: " + new String(decryptedText));
