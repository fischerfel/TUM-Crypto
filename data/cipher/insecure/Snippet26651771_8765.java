KeyGenerator keyGenerator = KeyGenerator.getInstance("RC4");
SecretKey secretKey = keyGenerator.generateKey();
Cipher cipher = Cipher.getInstance("RC4");  
String mainKey=secretKey.toString();
String cipherkey=cipher.toString());
