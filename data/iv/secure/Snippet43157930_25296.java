FileOutputStream out = new FileOutputStream(mypath);
String defaultMessage = "Empty File";
int iterationCount = 1000;
int keyLength = 256;
int saltLength = keyLength / 8;
SecureRandom randomGenerator = new SecureRandom();
byte[] salt = new byte[saltLength];
randomGenerator.nextBytes(salt);
KeySpec keySpec = new PBEKeySpec(submittedPassword.toCharArray(), salt, iterationCount, keyLength);
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
SecretKey key = new SecretKeySpec(keyBytes, "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
byte[] iv = new byte[cipher.getBlockSize()];
randomGenerator.nextBytes(iv);
IvParameterSpec ivParams = new IvParameterSpec(iv);
cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
byte[] ciphertext = cipher.doFinal(defaultMessage.getBytes("UTF-8"));
String finalMessage = ciphertext.toString() + "]" + iv.toString() + "]" + salt.toString();
out.write(finalMessage.getBytes());
out.close();
