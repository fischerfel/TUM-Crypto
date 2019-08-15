public String Encrypt()
{

    try
    {
        KeyPairGenerator kpg=KeyPairGenerator.getInstance("RSA");
        kpg.initialize(512);//initialize key pairs to 512 bits ,you can also take 1024 or 2048 bits
        kp=kpg.genKeyPair();
        PublicKey publi=kp.getPublic();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publi);
        byte[]src=srci.getBytes();//converting source data into byte array
        cipherData = cipher.doFinal(src);//use this method to finally encrypt data
        srco=new String(cipherData);//converting byte array into string
    }
    catch(Exception e)
    {

    }
        return srco;
}
public String Decrypt(String cipherdata)
    {
        try
        {

        PrivateKey privatei=kp.getPrivate();//Generating private key
        Cipher cipheri=Cipher.getInstance("RSA");//Intializing 2nd instance of Cipher class
        cipheri.init(Cipher.DECRYPT_MODE, privatei);//Setting to decrypt_mode
        byte[] cipherDat = cipheri.doFinal(cipherData);//Finally decrypting data
        decryptdata=new String(cipherDat);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return decryptdata;
}
