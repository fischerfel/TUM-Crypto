SecretKeySpec skeySpec = new SecretKeySpec(getCryptoKeyByteArray(length=16)); 
Cipher encryptor = Cipher.getInstance("AES/CTR/NoPadding");

// Initialisation vector:
byte[] iv = new byte[encryptor.getBlockSize()];
SecureRandom.getInstance("SHA1PRNG").nextBytes(iv); // If storing separately
IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

encryptor.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec); 
byte[] encrypted = encryptor.doFinal(plain); 
