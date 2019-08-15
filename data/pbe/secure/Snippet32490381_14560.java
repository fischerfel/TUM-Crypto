SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
// note, the third argument should be set to a value as high as possible
// 10K is about the minimum nowadays
KeySpec ks = new PBEKeySpec(password, salt, 1024, 128);
SecretKey s = f.generateSecret(ks);
Key k = new SecretKeySpec(s.getEncoded(),"AES");
