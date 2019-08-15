byte[] keyStart = "qweroiwejrwoejlsifeoisrn".getBytes(); // Random character string

byte[] toEncrypt = myMessageString.getBytes();

keyGen = KeyGenerator.getInstance("AES");
sr = SecureRandom.getInstance("SHA1PRNG");
sr.setSeed(keyStart);
keyGen.init(128, sr);
SecretKey secretKey = keyGen.generateKey();
byte[] secretKeyByte = secretKey.getEncoded();

SecretKeySpec skeySpec = new SecretKeySpec(secretKeyByte, "AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
cipher.doFinal(toEncrypt);
