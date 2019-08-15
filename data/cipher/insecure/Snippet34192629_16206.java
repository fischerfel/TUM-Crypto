String  epsKey ="12345678";
    String  str ="hcs";
DESKeySpec desKeySpec = new DESKeySpec(epsKey.getBytes());
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
SecretKey key = keyFactory.generateSecret(desKeySpec);
Cipher ecipher = Cipher.getInstance("DES");
//ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
ecipher.init(Cipher.ENCRYPT_MODE, key);
byte[] enc = ecipher.doFinal(str.getBytes("UTF8"));
String encryptedString = new BASE64Encoder().encode(enc);

System.out.println(encryptedString);
