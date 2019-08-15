KeyGenerator kgenerator = KeyGenerator.getInstance("AES");
SecureRandom random = new SecureRandom();
kgenerator.init(128, random);
Key aeskey = kgenerator.generateKey();

KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
kpg.initialize(1024, random);
KeyPair kpa = kpg.genKeyPair();
PublicKey pubKey = kpa.getPublic();
PrivateKey privKey = kpa.getPrivate();    

// Encrypt the generated Symmetric AES Key using RSA cipher  
Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");            
rsaCipher.init(Cipher.WRAP_MODE, pubKey);
byte[] encryptedSymmKey = rsaCipher.wrap(aeskey);            
// RSA Decryption of Encrypted Symmetric AES key
rsaCipher.init(Cipher.UNWRAP_MODE, privKey);
Key decryptedKey = rsaCipher.unwrap(encryptedSymmKey, "AES", Cipher.SECRET_KEY);

System.out.println("Decrypted Key Length: " + decryptedKey.getEncoded().length * 8); // -> 128
