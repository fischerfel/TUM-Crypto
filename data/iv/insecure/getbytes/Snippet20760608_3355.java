String encrypted = "mPgzvJKbSFgP6nRRHNlTufscZiChL2KUYaNeSF27+Dg=";
String key = "9d6ea4d3e6f8c4f8";
String salt = "1c5dd32d7ba54bdd"; 
String transform = "AES/CBC/ISO10126PADDING";

IvParameterSpec ivspec = new IvParameterSpec(salt.getBytes());
SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
Cipher cipher = Cipher.getInstance(transform);
cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

byte[] encryptedBytes = DatatypeConverter.parseBase64Binary(encrypted); // hash to byte[] 
byte[] data = cipher.doFinal(encryptedBytes); 
System.out.println(new String(data));
