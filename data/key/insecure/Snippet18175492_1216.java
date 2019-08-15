String key = "1234567890123456";
byte[] encrypted_bytes = READ_DATA_FROM_FILE
SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
IvParameterSpec ivSpec = new IvParameterSpec(key.getBytes());
Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
String plain_text = new String(cipher.doFinal(encrypted_bytes));
