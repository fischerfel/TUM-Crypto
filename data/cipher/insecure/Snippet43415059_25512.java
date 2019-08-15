Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, secretKey);

byte[] stringBytes = message.getBytes("UTF8");
byte[] raw = cipher.doFinal(stringBytes);

BASE64Encoder encoder = new BASE64Encoder();
String base64 = encoder.encode(raw);
return base64;
