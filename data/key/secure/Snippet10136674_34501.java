KeyGenerator keygen = KeyGenerator.getInstance("AES");
SecureRandom sec = new SecureRandom(key.getBytes());
keygen.init(128, sec);
Key key = keygen.generateKey();
SecretKeySpec skeySpec = new SecretKeySpec(key.getEncoded(), "AES");
...
