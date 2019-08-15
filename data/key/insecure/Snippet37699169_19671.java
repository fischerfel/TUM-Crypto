String key = "Bar12345Bar12345"; // 128 bit key
// Create key and cipher
Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
Cipher cipher = Cipher.getInstance("AES");
// encrypt the text
cipher.init(Cipher.ENCRYPT_MODE, aesKey);
byte[] encrypted = cipher.doFinal(text.getBytes());
String e=new String(encrypted);
byte[] encrypted1 = cipher.doFinal(e.getBytes());
System.out.println(encrypted.length+" "+encrypted1.length);
System.out.println(e);
// decrypt the text
cipher.init(Cipher.DECRYPT_MODE, aesKey);
String decrypted = new String(cipher.doFinal(encrypted));
System.out.println(decrypted);
