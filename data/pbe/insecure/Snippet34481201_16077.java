Random random = new Random();
byte[] salt = new byte[16];
random.nextBytes(salt);
KeySpec spec = new PBEKeySpec("pass123".toCharArray(), salt, 65536, 128);
SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
byte[] hash = f.generateSecret(spec).getEncoded();
Base64.Encoder enc = Base64.getEncoder();
