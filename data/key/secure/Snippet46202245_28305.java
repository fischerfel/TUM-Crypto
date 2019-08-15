String encryptedKey = Base64.encodeToString(keyword.getBytes(), Base64.NO_WRAP);
Key key = new SecretKeySpec(encryptedKey.getBytes(), algorithm);
Cipher chiper = Cipher.getInstance("AES");
chiper.init(Cipher.ENCRYPT_MODE, key);
byte[] encVal = chiper.doFinal(plainText.getBytes());
String encryptedValue = Base64.encodeToString(encVal, Base64.NO_WRAP);
return encryptedValue;
