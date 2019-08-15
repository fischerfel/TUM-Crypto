public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, 
InvalidKeyException, IllegalBlockSizeException, BadPaddingException  {

KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
keyGenerator.init(128);
Key aesKey = keyGenerator.generateKey();



KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
keyPairGenerator.initialize(1024);
KeyPair keyPair = keyPairGenerator.genKeyPair();

Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

byte[] aesKeyBytes = aesKey.getEncoded();
System.out.println("1. aesKeyBytes= "+ bytesToHex(aesKeyBytes));

cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
byte[] cipherText = cipher.doFinal(aesKeyBytes);
System.out.println("2. cipherText= "+bytesToHex(cipherText));

cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
byte[] decryptedKeyBytes = cipher.doFinal(cipherText);
System.out.println("3. decryptedKeyBytes= "+bytesToHex(decryptedKeyBytes));


//use symmetric with the decrypted key
SecretKey newAesKey = new SecretKeySpec(decryptedKeyBytes, "AES");
