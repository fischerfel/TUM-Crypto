Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
KeyGenerator keyGen = KeyGenerator.getInstance("AES");
Key secKey = keyGen.generateKey();
cipher.init(Cipher.ENCRYPT_MODE, secKey);
