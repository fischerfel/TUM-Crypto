SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
// What is salt ??
KeySpec spec = new PBEKeySpec("SOME_KEY", salt, 65536, 256);
SecretKey tmp = factory.generateSecret(spec);
SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
System.out.println(plaintext);
