SecureRandom sr = new SecureRandom()

key = new byte[16];
iv = new byte[16];

sr.nextBytes(key);
sr.nextBytes(iv);

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key,"AES"), new IvParameterSpec(IV));
