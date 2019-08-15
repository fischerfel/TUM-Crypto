SecretKeyFactory secKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
KeySpec spec = new PBEKeySpec(password, salt, iterations, 128);
SecretKey pbeSecretKey = secKeyFactory.generateSecret(spec);
SecretKey aesSecret = new SecretKeySpec(pbeSecretKey.getEncoded(), "AES");

Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding","BC");
