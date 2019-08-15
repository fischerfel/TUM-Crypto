byte[] salt = new byte[16];
random.nextBytes(salt);
KeySpec spec = new PBEKeySpec("password".toCharArray(), salt, 65536, 128);
SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] hash = f.generateSecret(spec).getEncoded();
Base64.Encoder enc = Base64.getEncoder();
System.out.printf("salt: %s%n", enc.encodeToString(salt));
System.out.printf("hash: %s%n", enc.encodeToString(hash));
