String vParameter= "Lq4aURUiyvKvEZBWMWpUr2wRSMu96E+J1UeHLTOhKEM=";  //The string that needs to be decoded.
byte[] encryptedV = Base64.decodeBase64(vParameter.getBytes("ASCII"));
String salt = "jkjkyt4"; // the i parameter - user’s id
String password = "^hjkh673!v@!a89mz+%5rT"; // application specific
MessageDigest digester = MessageDigest.getInstance("SHA-1");
digester.update((salt + password).getBytes("UTF-8"));
byte[] key = digester.digest();
SecretKeySpec secretKey = new SecretKeySpec(key, 2, 16, "AES");
String appIV = "SampleIV12345678";// application specific
IvParameterSpec iv= new IvParameterSpec(appIV.getBytes("UTF-8"));
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
byte[] decryptedV = cipher.doFinal(encryptedV, 0, encryptedV.length);
String v = new String(decryptedV, "UTF-8");
System.out.println(v); // foobarfoobarfoobarfoobarfoobar
