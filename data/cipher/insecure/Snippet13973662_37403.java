SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
String key = "abcdefg";
DESKeySpec keySpec = new DESKeySpec(key.getBytes());
SecretKey _key = kf.generateSecret(keySpec);
String xform = "DES";
Cipher cipher = Cipher.getInstance(xform);
byte[] IV = { 11, 22, 33, 44, 55, 66, 77, 88, 99, 18, 69, 17, 72, 94, 18, 30 };
IvParameterSpec ips = new IvParameterSpec(IV);
cipher.init(Cipher.DECRYPT_MODE, _key, ips);
String cipherText;
//cipher text is read from file.
byte[] plainText = cipher.doFinal(cipherText.getBytes());
