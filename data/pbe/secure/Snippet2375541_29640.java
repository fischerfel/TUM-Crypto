SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec ks = new PBEKeySpec(password,salt,1024,128);
SecretKey s = f.generateSecret(ks);
Key k = new SecretKeySpec(s.getEncoded(),"AES");
