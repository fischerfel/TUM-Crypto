// Generate key pair.
KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
kpg.initialize(1024, new SecureRandom());
KeyPair keyPair = kpg.genKeyPair();
PublicKey publicKey = keyPair.getPublic();
PrivateKey privateKey = keyPair.getPrivate();

// Data to encode/decode.
byte[] original = "The quick brown fox jumps over the lazy dog.".getBytes("UTF8");

// Encode data with public key.
Cipher cipherEncoder = Cipher.getInstance("RSA/ECB/NoPadding");
cipherEncoder.init(Cipher.ENCRYPT_MODE, publicKey);
byte[] encodedData = cipherEncoder.doFinal(original);

// Decode data with private key.
Cipher cipherDecoder = Cipher.getInstance("RSA/ECB/NoPadding");
cipherDecoder.init(Cipher.DECRYPT_MODE, privateKey);
byte[] decodedData = cipherEncoder.doFinal(encodedData);

// Output.
System.out.println(new String("Original data:   " + new String(original, "UTF8")));
System.out.println(new String("Encoded/decoded: " + new String(decodedData, "UTF8")));
