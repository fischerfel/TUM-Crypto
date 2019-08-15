private SecretKey decryptAESKey(byte[] data )
{
    SecretKey key = null;
    PrivateKey privKey = null;
    Cipher cipher = null;

    System.out.println ( "Data as hex: " + utility.asHex(data) );
    System.out.println ( "data length: " + data.length );
    try
    {
        // assume this loads our private key
        privKey = (PrivateKey)utility.loadLocalKey("private.key", false);

        cipher = Cipher.getInstance("RSA/ECB/NOPADDING");
        cipher.init(Cipher.DECRYPT_MODE, privKey );
        key = new SecretKeySpec(cipher.doFinal(data), "AES");

        System.out.println ( "Key decrypted, length is " + key.getEncoded().length );
        System.out.println ( "data: " + utility.asHex(key.getEncoded()));
    }
    catch(Exception e)
    {
        System.out.println ( "exception decrypting the aes key: " + e.getMessage() );
        e.printStackTrace();
        return null;
    }

    return key;
}
