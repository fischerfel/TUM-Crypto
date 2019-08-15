byte iv[] = new byte[16];
SecureRandom secRandom = new SecureRandom() ;
secRandom.nextBytes(iv);       
Cipher decipher = Cipher.getInstance("AES/CTR/NoPadding");
decipher.init(Cipher.DECRYPT_MODE, aesKey,new IvParameterSpec(iv)); 
byte[] cipherTextInByteArr =Base64.getDecoder().decode(encodedText);
byte[] plainTextInByteArr = decipher.doFinal(cipherTextInByteArr);     
String result=new String(plainTextInByteArr,"UTF-8");
