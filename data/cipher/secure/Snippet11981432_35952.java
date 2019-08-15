// Generate a new AES key
byte[] key = null;
try {
    KeyGenerator keygen = KeyGenerator.getInstance("AES");
    keygen.init(128);            
    key = keygen.generateKey().getEncoded();
}
catch (NoSuchAlgorithmException e) {}

// Set up modulus and exponent
String mod = "qgx5606ADkXRxndzurIRa5GDxzDYg5Xajeym7I8BXG1HBSzaaGmX+rjQfZK1h4JtQU+Xaowsc81mgJU8+gwneQa56r1bl6/5jFue4FsdXKfpau5az8rY2SAHKcOeyHAOsT9ZqcNa1x6cL/jl9P3cBtOzMk51Hk/w6VNoQ5JJo/0m/eAJzlhVKr2xbOYFhd0xp3qUgRuK8TN4TsSvfc+R1LOWc8+3H22Zj3vhBxSqSgeXxdxi7ThiGiAl6HUwMf8ph7FHNJvoUQq+QPL6dx77pu6xVFiHv1JOfpbKcOubn0VSPLYKY3QPKCzNMYQ6pxUDqzpGtydHR1xaX5K0FGTraw==";

String ex = "AQAB";
BigInteger modulus = new BigInteger(Base64.decode(mod, Base64.DEFAULT));
BigInteger exponent = new BigInteger(Base64.decode(ex, Base64.DEFAULT));

// Encrypt the AES key
PublicKey pubKey;
byte[] cipherData;
try {
    pubKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpe(modulus, exponent));
    Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");     
    rsaCipher.init(Cipher.ENCRYPT_MODE, pubKey);
    // The following line fails with:
    // org.bouncycastle.crypto.DataLengthException
    cipherData = rsaCipher.doFinal(key);     
}
catch (InvalidKeySpecException e) {}
catch (NoSuchAlgorithmException e) {}
catch (InvalidKeyException e) {}
catch (NoSuchPaddingException e) {}
catch (BadPaddingException e) {}
catch (IllegalBlockSizeException e) {}
