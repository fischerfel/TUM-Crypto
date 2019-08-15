SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, 1000, 384);
Key secretKey = factory.generateSecret(pbeKeySpec);
byte[] key = new byte[32];
byte[] iv = new byte[16];
System.arraycopy(secretKey.getEncoded(), 0, key, 0, 32);
System.arraycopy(secretKey.getEncoded(), 32, iv, 0, 16);

SecretKeySpec secret = new SecretKeySpec(key, "AES");
AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
byte[] result = cipher.doFinal("asdfasdf".getBytes("UTF-8"));
