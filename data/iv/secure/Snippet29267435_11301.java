SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
byte[] iv = new byte[cipher.getBlockSize()];
randomSecureRandom.nextBytes(iv);

IvParameterSpec ivParams = new IvParameterSpec(iv);
