byte[] iv = new byte[cipher.getBlockSize()];
new SecureRandom().nextBytes(iv);
ivspec = new IvParameterSpec(iv);
