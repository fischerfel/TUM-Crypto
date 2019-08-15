SecureRandom rng = new SecureRandom();

byte[] aesKeyData = new byte[128 / Byte.SIZE];
rng.nextBytes(aesKeyData);
SecretKey aesKey = new SecretKeySpec(aesKeyData, "AES");

// just to show it works
Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
byte[] iv = new byte[aesCBC.getBlockSize()];
rng.nextBytes(iv);
aesCBC.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(iv));
