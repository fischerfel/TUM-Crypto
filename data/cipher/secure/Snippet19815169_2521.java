  // input data
  byte[] secretKey = { ... };
  byte[] encryptedData = { ... };

  // decryption
  SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
  random.setSeed(secretKey);
  KeyGenerator keyGen = KeyGenerator.getInstance("AES");
  keyGen.init(128, random);
  SecretKey sk = keyGen.generateKey();
  SecretKeySpec keySpec = new SecretKeySpec(sk.getEncoded(), "AES");
  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
  IvParameterSpec IV16 = new IvParameterSpec(keySpec.getEncoded());
  cipher.init(Cipher.DECRYPT_MODE, keySpec, IV16);
  byte[] decrypted = cipher.doFinal(encryptedData);
