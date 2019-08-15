byte[] salt = new byte[16];
random.nextBytes(salt);
KeySpec spec = new PBEKeySpec("password".toCharArray(), salt, 2048, 160);
SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] hash = f.generateSecret(spec).getEncoded();
System.out.println("salt: " + new BigInteger(1, salt).toString(16));
System.out.println("hash: " + new BigInteger(1, hash).toString(16));
