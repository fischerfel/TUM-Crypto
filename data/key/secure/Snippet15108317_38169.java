byte[] valueData = value.getBytes();   
byte[] keyData = skey.getBytes();

SecretKeySpec skeySpec = new SecretKeySpec(keyData, "AES");
Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7PADDING");
cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

byte[] valueEncryptedData = cipher.doFinal(valueData);
String valueEncryptedString = Base64.encodeToString(valueEncryptedData, Base64.DEFAULT);

return valueEncryptedString;
