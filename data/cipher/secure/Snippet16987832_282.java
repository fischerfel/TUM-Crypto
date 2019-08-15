 public static byte[] Encrypt_RSA(byte[] plaintext,PublicKey key) throws
        InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
        IllegalBlockSizeException, BadPaddingException,NoSuchProviderException{
   Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    c.init(Cipher.ENCRYPT_MODE,key);
    byte [] ciphertext = c.doFinal(plaintext);
    return ciphertext;
}

public static byte[] Decrypt_RSA(byte []ciphertext,PrivateKey key) throws
       NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
       IllegalBlockSizeException, BadPaddingException,NoSuchProviderException{
    Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    c.init(Cipher.DECRYPT_MODE,key);
    byte [] plaintext=c.doFinal(ciphertext);
    return plaintext;
}

public static byte[] Encrypt_RSA_Pr(byte[] plaintext,PrivateKey key) throws
       NoSuchAlgorithmException, NoSuchPaddingException,
       IllegalBlockSizeException, BadPaddingException, InvalidKeyException{
    Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    c.init(Cipher.ENCRYPT_MODE,key);
    byte [] ciphertext = c.doFinal(plaintext);
    return ciphertext;
}

 public static byte[] Decrypt_RSA_Pub(byte []ciphertext,PublicKey key) throws
        NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
        IllegalBlockSizeException,BadPaddingException,NoSuchProviderException{
    Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    c.init(Cipher.DECRYPT_MODE,key);
    byte [] plaintext=c.doFinal(ciphertext);
    return plaintext;
}
