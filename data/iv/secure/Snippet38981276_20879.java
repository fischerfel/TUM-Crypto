SecureRandom r = new SecureRandom();
byte[] ivBytes = new byte[16];
r.nextBytes(ivBytes);

cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ivBytes));
