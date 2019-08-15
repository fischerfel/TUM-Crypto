public byte[] encrypt(byte[] plaintext){
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    return cipher.doFinal(plaintext);
}

public byte[] decrypt(byte[] ciphertext){
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    return cipher.doFinal(ciphertext);
}
