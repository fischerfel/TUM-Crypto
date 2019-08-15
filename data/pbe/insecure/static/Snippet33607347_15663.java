PBEKeySpec pbeKeySpec = new PBEKeySpec("password".toCharArray());
SecretKeyFactory keyFac = SecretKeyFactory.getInstance(ALGORITHM);
SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
PBEParameterSpec pbeParamSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

// Create PBE Cipher
Cipher pbeCipher = Cipher.getInstance(ALGORITHM);

// Initialize PBE Cipher with key and parameters
pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

byte[] encrypted = pbeCipher.doFinal("text to be encrypted");
