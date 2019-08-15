String text = "abc";
String key = "Bar12345Bar12345";
Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, aesKey);
byte[] encrypted = cipher.doFinal(text.getBytes());
Base64.Encoder encoder = Base64.getEncoder();
String encryptedString = encoder.encodeToString(encrypted);
System.out.println(encryptedString);
