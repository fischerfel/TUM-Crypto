Cipher cipher = Cipher.getInstance("DESede");
byte[] bytes = cipher.doFinal(value.getBytes());
String decrypted = new String(bytes);[code]
