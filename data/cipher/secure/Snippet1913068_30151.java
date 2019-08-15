String plaintext = "hello";
String key = "1234567890123456";

SecretKey keyspec = new SecretKeySpec(key.getBytes(), "AES");       

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE,keyspec);
byte[] encrypted = cipher.doFinal(plaintext.getBytes());

BASE64Encoder base64 = new BASE64Encoder();
String encodedString = base64.encode(encrypted);

System.out.println(encodedString);
