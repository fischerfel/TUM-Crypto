SecureRandom rnd = new SecureRandom();
IvParameterSpec iv = new IvParameterSpec(rnd.generateSeed(16));

KeyGenerator generator = KeyGenerator.getInstance("AES");
generator.init(256);
SecretKey k = generator.generateKey();

Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
c.init(Cipher.ENCRYPT_MODE, k, iv);
