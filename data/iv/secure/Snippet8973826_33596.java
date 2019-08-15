//Generate a key
KeyGenerator KeyGen = KeyGenerator.getInstance("AES");
KeyGen.init(128);
SecretKey Key = KeyGen.generateKey();

//Generate init vector
SecureRandom rng = SecureRandom.getInstance("SHA1PRNG");
byte [] IV = new byte[16];
rng.nextBytes(IV);

//Initialize the encryptor
Cipher ci = Cipher.getInstance("AES/CBC/NoPadding"); //The spec might be different!
ci.init(Cipher.ENCRYPT_MODE, Key, new IvParameterSpec(IV));
