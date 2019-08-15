KeyGenerator keyGen = KeyGenerator.getInstance("AES");
keyGen.init(512);
SecretKey secretKey = keyGen.generateKey();
final int AES_KEYLENGTH = 512;  
byte[] iv = new byte[AES_KEYLENGTH / 8];    
SecureRandom prng = new SecureRandom();
prng.nextBytes(iv);
Cipher aesCipherForEncryption = Cipher.getInstance("AES/CBC/PKCS7PADDING");
