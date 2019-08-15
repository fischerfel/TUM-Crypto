Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
SecureRandom secureRandom = new SecureRandom();

byte[] keyBytes = new byte[16];
secureRandom.nextBytes(keyBytes);
SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

byte[] plain = new byte[256];
secureRandom.nextBytes(plain);

// first init using random IV (save it for later)
cipher.init(Cipher.ENCRYPT_MODE, key, secureRandom);
byte[] realIv = cipher.getIV();
byte[] expected = cipher.doFinal(plain);

// now init using dummy IV and encrypt with real IV prefix
IvParameterSpec nullIv = new IvParameterSpec(new byte[16]);
cipher.init(Cipher.ENCRYPT_MODE, key, nullIv);
// calculate equivalent iv
Cipher equivalentIvAsFirstBlock = Cipher.getInstance("AES/CBC/NoPadding");
equivalentIvAsFirstBlock.init(Cipher.DECRYPT_MODE, key, nullIv);
byte[] equivalentIv = equivalentIvAsFirstBlock.doFinal(realIv);

cipher.update(equivalentIv);
byte[] result = cipher.doFinal(plain);
System.out.println(Arrays.equals(expected, result));
