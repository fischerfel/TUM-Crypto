// encode the SecretKeySpec
private byte[] EncryptSecretKey ()
{
    Cipher cipher = null;
    byte[] key = null;

    try
    {
        cipher = Cipher.getInstance("RSA/ECB/NOPADDING");
        // contact.getPublicKey returns a public key of type Key
        cipher.init(Cipher.ENCRYPT_MODE, contact.getPublicKey() );
        // skey is the SecretKey used to encrypt the AES data
        key = cipher.doFinal(skey.getEncoded());
    }
    catch(Exception e )
    {
        System.out.println ( "exception encoding key: " + e.getMessage() );
        e.printStackTrace();
    }
    return key;
}
