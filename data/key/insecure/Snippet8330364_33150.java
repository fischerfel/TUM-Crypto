Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
String key = "aaaaaaaaaaaaaaaa";
String textToEncryptpt = "1234567890123456";

SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
IvParameterSpec ivspec = new IvParameterSpec(key.getBytes());
cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
byte[] encrypted = cipher.doFinal(textToEncryptpt.getBytes());
System.out.println(Crypto.bytesToHex(encrypted));
