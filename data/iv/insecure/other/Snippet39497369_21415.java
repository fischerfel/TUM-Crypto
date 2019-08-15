static
{
    try
    {
        //ciphers initialization 

        SecretKey secretKey = THE_KEY;

        //Decryption cipher
        Cipher dec = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] ivDec = new byte[dec.getBlockSize()];
        IvParameterSpec ivparDec = new IvParameterSpec(ivDec);
        dec.init(Cipher.DECRYPT_MODE, secretKey,ivparDec);

        //Encryption cipher
        Cipher enc = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[enc.getBlockSize()];
        IvParameterSpec ivpar = new IvParameterSpec(iv);
        enc.init(Cipher.ENCRYPT_MODE, secretKey,ivpar);
    }
    catch (Exception e){e.printStackTrace();}
}
