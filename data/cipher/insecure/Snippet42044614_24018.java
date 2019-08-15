Cipher cipher = Cipher.getInstance("DESede");
byte[] bytes = cipher.doFinal(value.getBytes());
String encrypted = new String(bytes);
