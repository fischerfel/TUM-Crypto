PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(),  iterations, keyLength);
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndTripleDES");
SecretKey key = keyFactory.generateSecret(keySpec);
Cipher cipher = Cipher.getInstance(key.getAlgorithm());
CipherInputStream cis = new CipherInputStream(is, cipher);
cipher.init(Cipher.ENCRYPT_MODE, key);
