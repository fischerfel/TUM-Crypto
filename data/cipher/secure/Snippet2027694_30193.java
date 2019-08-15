KeyPair keys = KeyPairGenerator.getInstance("RSA").generateKeyPair();
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, keys.getPublic());
byte[] encrypted = cipher.doFinal(rawData);
