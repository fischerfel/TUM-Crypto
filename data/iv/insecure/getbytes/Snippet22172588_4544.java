String PUBLIC_KEY = "abcdefghijklmnio";
String PUBLIC_IV = "abcdefghijklmnio";
byte[] byteArr = PUBLIC_KEY.getBytes();
Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
final SecretKeySpec secretKey = new SecretKeySpec(byteArr, "AES");
cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(PUBLIC_IV.getBytes()));

byte[] parsed = Base64.decodeBase64(encrypted.getBytes());
//byte[] parsed = DatatypeConverter.parseBase64Binary(encrypted); 

byte[] fin = cipher.doFinal(parsed);
String decryptedString = new String(fin);
