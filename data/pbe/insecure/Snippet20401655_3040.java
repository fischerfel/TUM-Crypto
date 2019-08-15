byte[] salt = "choose a better salt".getBytes();
int iterations = 10000;
SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
SecretKey tmp = factory.generateSecret(new PBEKeySpec(passphrase.toCharArray(), salt, iterations, 128));
SecretKeySpec key = new SecretKeySpec(tmp.getEncoded(), "AES");
