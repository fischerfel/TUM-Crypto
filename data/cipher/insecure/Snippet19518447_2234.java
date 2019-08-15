Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, keySpec); 
byte[] encryptedTextBytes = cipher.doFinal(message .getBytes("UTF-8")); 
String k = new String(encryptedTextBytes); 
System.out.println("KKKKK"+k);
