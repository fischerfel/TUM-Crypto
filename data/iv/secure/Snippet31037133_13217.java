SecureRandom r = new SecureRandom();
byte[] ivBytes = new byte[16];
r.nextBytes(ivBytes);

cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
