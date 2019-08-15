SecretKey key = new SecretKeySpec(keyBytes, "DES");
Cipher ecipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
byte[] utf8 = str.getBytes("UTF8");
byte[] enc = ecipher.doFinal(utf8);
// Encode bytes to base64 to get a string
return  new String(Base64Utils.encode(enc));
