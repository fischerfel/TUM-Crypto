byte[] encrypted = DB.getThatEncryptedData();
byte[] salt = Arrays.copyOfRange(encrypted, 4, 35);

String password = SomewhereSafe.getThePassword();
KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100000, 32);
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
SecretKey secret = keyFactory.generateSecret(spec);

Cipher c = Cipher.getInstance("AES/CTR/NoPadding");
c.init(Cipher.DECRYPT_MODE, secret);
byte[] decrypted = c.doFinal(encrypted);
