KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");      
SecureRandom random = SecureRandom.getInstance("IBMSecureRandom", "IBMJCE");        
random.setSeed(longToBytes(System.currentTimeMillis()));
keyGen.initialize(512, random);

KeyPair pairTytus = keyGen.generateKeyPair();
KeyPair pairRomek = keyGen.generateKeyPair();
KeyPair pairAtomek = keyGen.generateKeyPair();

// Making a wrap-key for private keys; based on password.
byte[] key = ("password").getBytes("UTF-8");
MessageDigest sha = MessageDigest.getInstance("SHA-1");
key = sha.digest(key);
key = Arrays.copyOf(key, 16); // use only first 128 bit

SecretKeySpec secretKeySpec = new SecretKeySpec(key, "MARS");

Cipher c1 = Cipher.getInstance("MARS/ECB/NoPadding");
c1.init(Cipher.WRAP_MODE, secretKeySpec);

c1.wrap(pairTytus.getPrivate());
