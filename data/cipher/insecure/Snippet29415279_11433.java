KeyGenerator keyGen = KeyGenerator.getInstance("DES");
SecretKey secretKey = keyGen.generateKey();
Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
desCipher.init(Cipher.ENCRYPT_MODE,secretKey);
/* Encryption */
strDataToEncrypt = "Hello World of Encryption using DES ";
byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
byte[] byteCipherText = desCipher.doFinal(byteDataToEncrypt); 
strCipherText = new BASE64Encoder().encode(byteCipherText);
System.out.println("Cipher Text generated using DES with CBC mode and PKCS5 Padding is " +strCipherText);
/* Decryption */
desCipher.init(Cipher.DECRYPT_MODE,secretKey,desCipher.getParameters());

byte[] byteDecryptedText = desCipher.doFinal(byteCipherText);
strDecryptedText = new String(byteDecryptedText);
System.out.println(" Decrypted Text message is " +strDecryptedText);
