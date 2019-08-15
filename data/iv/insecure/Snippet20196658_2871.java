String SecretKey = "0123456789abcdef";
String iv = "fedcba9876543210";

IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
SecretKeySpec keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

//Initialize the cipher
cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec); 

String message = "What's up?";
byte[] encrypted = cipher.doFinal(message.getBytes());

//Send the data
outputStream.write(encrypted);
