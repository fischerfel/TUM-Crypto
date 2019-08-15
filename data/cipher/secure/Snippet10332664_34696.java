// Use RSA/NONE/NoPadding as algorithm and BouncyCastle as crypto provider
Cipher asymmetricCipher = Cipher.getInstance("RSA/NONE/NoPadding", "BC");

// asume, that publicKeyBytes contains a byte array representing
// your public key
X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);

KeyFactory keyFactory;
keyFactory = KeyFactory.getInstance(publicKeySpec.getFormat());
Key key = keyFactory.generatePublic(publicKeySpec);

// initialize your cipher
asymmetricCipher.init(Cipher.DECRYPT_MODE, key);
// asuming, cipherText is a byte array containing your encrypted message
byte[] plainText = asymmetricCipher.doFinal(cipherText);
