SecureRandom random = new SecureRandom();
byte[] initVector   = new BigInteger(96, random).toByteArray();
byte[] data         = new BigInteger(255, random).toByteArray();
byte[] key          = new BigInteger(255, random).toByteArray();
byte[] encrypted    = new byte[data.length];

final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(16 * Byte.SIZE, initVector));
cipher.update(data, 0, data.length, encrypted, 0);
byte[] tag = cipher.doFinal();
