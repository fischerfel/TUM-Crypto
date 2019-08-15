String key = "test";
String in = "ijzLe/2WgbaP+n3YScQSgQ==";

SecretKeySpec skeySpec = new SecretKeySpec(md5(key).getBytes(), "AES");

Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
cipher.init(Cipher.DECRYPT_MODE, skeySpec);

byte[] encryptedByteArray = Base64.decode(in.getBytes(),0);
byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);
String decryptedData = new String(Base64.decode(decryptedByteArray, 0));

Log.v("NOTE","Data: "+decryptedData);
