SecureRandom m = SecureRandom.getInstance("SHA1PRNG");
//Secure Hash
MessageDigest hash = MessageDigest.getInstance("SHA-1");
//get a KeyGenerator DES init with a PRNG
KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
keyGenerator.init(m);
//get Masterkey
Key masterkey = keyGenerator.generateKey();
//Get a DES cipger
Cipher cipher = Cipher.getInstance("DES");
//AND HERE HOW I RESOLVED!
SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(hash.digest(passphrase.getBytes()), 8), "DES");
