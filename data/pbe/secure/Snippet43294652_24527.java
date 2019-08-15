final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
final PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, hashLength);
final SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
return secretKey.getEncoded();
