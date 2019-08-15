/* Derive the key, given passphrase and salt. */
final String plainText = "some value";
final String passphrase = "some pass";
final byte[] salt = {1, 1, 1, 1, 1, 1, 1, 1, 1};
final int iterations = 1000;
byte[] iv = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt, iterations, 128);
SecretKey tmp = factory.generateSecret(keySpec);
SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(iv));
byte[] ciphertext = cipher.doFinal(plainText.getBytes("UTF-8"));

System.out.println(DatatypeConverter.printBase64Binary(ciphertext));
