IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
// Cipher is not thread safe
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
byte[] encryptedByte=cipher.doFinal(clearText); 
String encrypedValue = new String(Base64.encodeBase64(encryptedByte));
