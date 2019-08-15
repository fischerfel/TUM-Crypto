Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
String password = "kallisti"; // only 8, 12, or 16 chars will work as a key
byte[] key = password.getBytes(Charset.forName("UTF-16LE"));
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"),
    new IvParameterSpec(key));
return cipher; // then use CipherInputStream(InputStream, Cipher)
