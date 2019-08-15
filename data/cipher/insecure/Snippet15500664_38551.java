byte[] keyBytes = new byte[16];
keyBytes[0] = 1;
SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
String input = "hello";
Cipher cipher;
byte[] bytes = null;
cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, key);
bytes = cipher.doFinal(input.getBytes("UTF-8"));

System.out.println("Encoded: "+Arrays.toString(bytes));

cipher.init(Cipher.DECRYPT_MODE, key);
byte[] decoded = cipher.doFinal(bytes);

System.out.println("Decoded: "+new String(decoded, "UTF-8"));
