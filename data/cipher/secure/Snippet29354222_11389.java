SecureRandom random = new SecureRandom();
byte[] salt = new byte[16];
random.nextBytes(salt);

KeySpec spec = new PBEKeySpec("password".toCharArray(), salt, 65536, 256); // AES-256
SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] key = f.generateSecret(spec).getEncoded();

byte[] ivBytes = new byte[16];
random.nextBytes(ivBytes);
IvParameterSpec iv = new IvParameterSpec(ivBytes);

Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
c.init(Cipher.ENCRYPT_MODE, key, iv);
byte[] encValue = c.doFinal(valueToEnc.getBytes());

byte[] finalCiphertext = new byte[encValue.length+2*16];
System.arraycopy(ivBytes, 0, finalCiphertext, 0, 16);
System.arraycopy(salt, 0, finalCiphertext, 16, 16);
System.arraycopy(encValue, 0, finalCiphertext, 32, encValue.length);

return finalCiphertext;
