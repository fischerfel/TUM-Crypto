String secret = "secret";
byte[] cipherText = encrypted_string.getBytes("UTF8");
SecretKey secKey = new SecretKeySpec(secret.getBytes(), "AES");
Cipher aesCipher = Cipher.getInstance("AES");
aesCipher.init(Cipher.DECRYPT_MODE, secKey);
byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
String myDecryptedText = = new String(bytePlainText);
