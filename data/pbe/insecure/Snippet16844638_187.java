String filename = new String("foo");
String salt = new String(Base64.encodeBase64(filename.getBytes()));
SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
PBEKeySpec spec = new PBEKeySpec(filename.toCharArray(),salt.getBytes(), 1024,128);
SecretKey tmp = factory.generateSecret(spec);
SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
System.out.println(new String(Base64.encodeBase64(secret.getEncoded())));
