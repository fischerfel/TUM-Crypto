byte[] iv = new byte[16];
SecureRandom random = new SecureRandom();
random.nextBytes(iv);
IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
AesCipher.init(Cipher.ENCRYPT_MODE, SecKey, ivParameterSpec);
