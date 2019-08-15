public static byte[] encryptRSA(byte[] text, PublicKey key) throws Exception {

byte[] cipherText = null;
// get an RSA cipher object and print the provider
Cipher cipher = Cipher.getInstance("RSA");

// encrypt the plaintext using the public key
cipher.init(Cipher.ENCRYPT_MODE, key);
cipherText = cipher.doFinal(text);
return cipherText;

}

public static byte[] decryptRSA(byte[] text, PrivateKey key) throws Exception {

byte[] dectyptedText = null;
// decrypt the text using the private key
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE, key);
dectyptedText = cipher.doFinal(text);
return dectyptedText;

}
