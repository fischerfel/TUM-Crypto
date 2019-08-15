 Cipher cipher = Cipher.getInstance("RSA");
 cipher.init(Cipher.WRAP_MODE, publicKey);
 SecretKey secret = (SecretKey) keyStore.getKey("key",null);
 byte[] encrypted = cipher.wrap(secret);
