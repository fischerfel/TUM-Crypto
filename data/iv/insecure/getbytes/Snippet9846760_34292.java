// I use String.getBytes() as a convenince a lot.  I specify the encoding
// to ensure that the result is consistent.
final String utf8 = "utf-8";

String password = "It's a secret!  Make sure it's long enough (24 bytes)";
byte[] keyBytes = Arrays.copyOf(password.getBytes(utf8), 24);
SecretKey key = new SecretKeySpec(keyBytes, "DESede");

// Your vector must be 8 bytes long
String vector = "ABCD1234";
IvParameterSpec iv = new IvParameterSpec(vector.getBytes(utf8));

// Make an encrypter
Cipher encrypt = Cipher.getInstance("DESede/CBC/PKCS5Padding");
encrypt.init(Cipher.ENCRYPT_MODE, key, iv);

// Make a decrypter
Cipher decrypt = Cipher.getInstance("DESede/CBC/PKCS5Padding");
decrypt.init(Cipher.DECRYPT_MODE, key, iv);

// Example use
String message = "message";
byte[] messageBytes = message.getBytes(utf8);
byte[] encryptedByted = encrypt.doFinal(messageBytes);
byte[] decryptedBytes = decrypt.doFinal(encryptedByted);

// You can re-run the exmaple to see taht the encrypted bytes are consistent
System.out.println(new String(messageBytes, utf8));
System.out.println(new String(encryptedByted, utf8));
System.out.println(new String(decryptedBytes, utf8));
