KeyGenerator generator = KeyGenerator.getInstance("AES/CTR/PKCS5PADDING");
generator.init(128);
SecretKey key = generator.generateKey();
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, key);
...
