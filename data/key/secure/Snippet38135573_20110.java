SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
cipher.init(1, secretKeySpec);
byte[] aBytes = cipher.doFinal(inputString.getBytes());
BASE64Encoder encoder = new BASE64Encoder();
String base64 = encoder.encode(aBytes).toString();
base64 = URLEncoder.encode(base64, "UTF-8");
return base64;
