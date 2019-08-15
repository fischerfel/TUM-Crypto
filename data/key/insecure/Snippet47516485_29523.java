String key = "Bar12345Bar12345";
Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
Cipher cipher = Cipher.getInstance("AES");
byte[] encrypted = text.getBytes(); 
cipher.init(Cipher.DECRYPT_MODE, aesKey);
String decrypted = new String(cipher.doFinal(encrypted));
System.err.println(decrypted);
