dcipher = Cipher.getInstance("PBEWithMD5AndDES");
KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
