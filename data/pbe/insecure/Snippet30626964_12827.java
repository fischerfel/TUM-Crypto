String passphrase = "test";
KeySpec keySpec = new PBEKeySpec(passphrase.toCharArray());
SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
ecipher = Cipher.getInstance(key.getAlgorithm());
...
ecipher.init(Cipher.ENCRYPT_MODE, ket, paramSpec);
...
