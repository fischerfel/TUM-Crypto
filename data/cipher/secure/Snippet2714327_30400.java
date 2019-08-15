public byte[] encrypt(byte[] input)  throws Exception
{
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//
    cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
    return cipher.doFinal(input);
}

public byte[] decrypt(byte[] input)  throws Exception
{   
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");///
    cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
    return cipher.doFinal(input);
}
