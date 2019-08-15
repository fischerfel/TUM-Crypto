 try {
   SecretKeyFactory factory = SecretKeyFactory
     .getInstance(KEY_DERIVATION_ALGORITHM);
   SecretKey tmp = factory.generateSecret(new PBEKeySpec(password,
     salt, getPBKDFIterations(), AES_256_KEY_SIZE * 8));
   return new SecretKeySpec(tmp.getEncoded(), AES_NAME);
  } catch (GeneralSecurityException e) {
   throw new CryptorException(String.format(
     "Failed to generate key from password using %s.",
     KEY_DERIVATION_ALGORITHM), e);
  }
