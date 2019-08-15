KeySpec keySpec= new DESedeKeySpec(bytesKey);
SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
SecretKey secretKey= secretKeyFactory.generateSecret(keySpec);
Cipher cipher = Cipher.getInstance("DESede");
cipher.init(modo, secretKey);
