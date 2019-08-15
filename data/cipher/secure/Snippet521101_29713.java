// Generate new key
KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
PrivateKey privateKey = keyPair.getPrivate();
String plaintext = "This is the message being signed";

// Compute signature
Signature instance = Signature.getInstance("SHA1withRSA");
instance.initSign(privateKey);
instance.update((plaintext).getBytes());
byte[] signature = instance.sign();

// Compute digest
MessageDigest sha1 = MessageDigest.getInstance("SHA1");
byte[] digest = sha1.digest((plaintext).getBytes());

// Encrypt digest
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, privateKey);
byte[] cipherText = cipher.doFinal(digest);

// Display results
System.out.println("Input data: " + plaintext);
System.out.println("Digest: " + bytes2String(digest));
System.out.println("Cipher text: " + bytes2String(cipherText));
System.out.println("Signature: " + bytes2String(signature));
