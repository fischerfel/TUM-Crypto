String key = "some 16 byte key";
byte[] keyBytes = key.getBytes("UTF-8");
byte[] plainBytes = plainText.getBytes("UTF-8");
SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
byte[] encrypted = cipher.doFinal(plainBytes);
String result = Base64.encodeBytes(encrypted);
return result;
