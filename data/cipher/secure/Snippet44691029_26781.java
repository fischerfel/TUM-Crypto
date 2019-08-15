KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
kpg.initialize(1024, new SecureRandom());
KeyPair kp = kpg.generateKeyPair();
PrivateKey privateKey = kp.getPrivate();
byte[] encryptedBytes = "SAMPLE".getBytes();
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE, privateKey);
byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
