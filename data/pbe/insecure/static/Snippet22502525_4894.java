/* Derive the key, given password and salt. */
SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec spec = new PBEKeySpec("password".toCharArray(), "salt".getBytes(), 65536, 128);
SecretKey tmp = factory.generateSecret(spec);
SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");


/* Encrypt the message. */
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secret);
AlgorithmParameters params = cipher.getParameters();
byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
byte[] ciphertext = cipher.doFinal("Hello, World! My data is here.. !".getBytes("UTF-8"));
System.out.println("cipher :"+new String(ciphertext));






/*String-key convertion */
String stringKey=Base64.encodeBase64String(secret.getEncoded());//To String key
byte[] encodedKey     = Base64.decodeBase64(stringKey.getBytes());
SecretKey originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");// Convert from string



/* Decrypt the message, given derived key and initialization vector. */
Cipher cipher1 = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher1.init(Cipher.DECRYPT_MODE, originalKey, new IvParameterSpec(iv));
String plaintext = new String(cipher1.doFinal(ciphertext), "UTF-8");
System.out.println(plaintext);
