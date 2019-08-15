byte[] key = "testKeyMaster123".getBytes("UTF-8");
SecretKeySpec _secretKey = new SecretKeySpec(key, "AES");
String input = "some.email.m@gmail.com";
Cipher cipher = Cipher.getInstance("AES");
byte[] bytes = input.getBytes("UTF-8");
cipher.init(Cipher.ENCRYPT_MODE, _secretKey);
byte[] output = cipher.doFinal(bytes);
result = Base64.encodeToString(output, Base64.DEFAULT);
