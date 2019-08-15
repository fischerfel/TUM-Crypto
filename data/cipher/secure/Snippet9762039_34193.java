private byte[] EncryptSecretKey ()
{
    Cipher cipher = null;
    byte[] key = null;

    try
    {
        // initialize the cipher with the user's public key
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, contact.getPublicKey() );
        key = cipher.doFinal(skey.getEncoded());
    }
    catch(Exception e )
    {
        System.out.println ( "exception encoding key: " + e.getMessage() );
        e.printStackTrace();
    }
    return key;
}
