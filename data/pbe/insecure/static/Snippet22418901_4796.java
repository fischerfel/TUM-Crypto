String password  = "password";
int iterationCount = 1000;
int keyLength = 256;
int saltLength = keyLength / 8; // same size as key output

SecureRandom random = new SecureRandom();
byte[] salt = new byte[saltLength];
randomb.nextBytes(salt);
KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                    iterationCount, keyLength);
SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");
byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
SecretKey key = new SecretKeySpec(keyBytes, "AES");

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
byte[] iv = new byte[cipher.getBlockSize());
random.nextBytes(iv);
IvParameterSpec ivParams = new IvParameterSpec(iv);
cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));
