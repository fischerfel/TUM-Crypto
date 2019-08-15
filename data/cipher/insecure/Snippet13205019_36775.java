SecureRandom random = new SecureRandom(); // quite heavy, look into a lighter method.

String stringToEncrypt = "mypassword";
byte[] realiv = new byte[16];
random.nextBytes(realiv);
Cipher ecipher = Cipher.getInstance("AES");

SecureRandom random = new SecureRandom(); // quite heavy, look into a lighter method.

byte[] realiv = new byte[16];
random.nextBytes(realiv);       

byte[] secret = "somelongsecretkey".getBytes();
SecretKeySpec secretKey = new SecretKeySpec(secret, "AES");
ecipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
byte[] encryptedData = ecipher.doFinal();
