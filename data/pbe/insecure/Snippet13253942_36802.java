 String key = "mysecretpassword";
 KeySpec spec = new PBEKeySpec(key.toCharArray(), Salt, 12345678,256);
 SecretKey encriptionKey = factory.generateSecret(spec);
 Key encriptionKey = new SecretKeySpec(encriptionKey.getEncoded(), "AES");
