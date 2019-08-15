KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
keyGen.initialize(2048);
KeyPair pair = keyGen.generateKeyPair();
PublicKey pubKey= pair.getPublic();
PrivateKey privateKey = pair.getPrivate();
Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
c1.init(Cipher.ENCRYPT_MODE, pubKey);
KeyGenerator aesKeyGenerator = KeyGenerator.getInstance("AES");
aesKeyGenerator.init(256);               
 Key aesKey = rijndaelKeyGenerator.generateKey();                
Cipher symmetricCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
byte[] encodedKeyBytes = c1.doFinal(aeslKey.getEncoded());
SecretKey aesKey1 = new SecretKeySpec(encodedKeyBytes, "aes1");
Cipher dec = Cipher.getInstance("RSA/ECB/PKCS1Padding");
dec.init(Cipher.DECRYPT_MODE, privateKey);
symmetricCipher.init(Cipher.DECRYPT_MODE, aesKey1, spec);                   
if(aesKey.getEncoded() == dec.doFinal(c1.doFinal(aesKey.getEncoded())) )
{                            
    // Not reaching here but is supposed to 
}
