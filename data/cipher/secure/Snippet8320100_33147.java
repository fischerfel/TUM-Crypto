// Encryption, client side
byte[] plainData = "hello plaintext!".getBytes("UTF-8");
byte[] salt = new byte[64];
new SecureRandom().nextBytes(salt);
KeySpec spec = new javax.crypto.spec.PBEKeySpec("password".toCharArray(), salt, 1024,   256);
SecretKey sk = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec);
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sk.getEncoded(), "AES"));
byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
byte[] ciphertext = cipher.doFinal(plainData);
System.out.println("ciphertext: "+new String(ciphertext, "UTF-8")); // cipher

// Decryption, server side
KeySpec spec2 = new javax.crypto.spec.PBEKeySpec("password".toCharArray(), salt, 1024, 256);
SecretKey sk2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec2);
Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5Padding");

cipher2.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sk2.getEncoded(), "AES"), new IvParameterSpec(iv)); // Get the same IV value from client/encryptor aswell, still random
String plaintext = new String(cipher2.doFinal(ciphertext), "UTF-8");
System.out.println("decrypted plaintext: "+plaintext); // plain
