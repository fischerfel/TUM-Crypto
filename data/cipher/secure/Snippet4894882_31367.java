    public static byte[] encrypt(byte[] text, PublicKey key) throws Exception
{
    byte[] cipherText = null;
    try
    {

        Cipher cipher = Cipher.getInstance("ElGamal/None/NoPadding", "BC"");
        if (_log.isDebugEnabled())
        {
            _log.debug("\nProvider is: " + cipher.getProvider().getInfo());
            _log.debug("\nStart encryption with public key");
        }

        // encrypt the plaintext using the public key
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(text);
    }
    catch (Exception e)
    {
        _log.error(e, e);
        throw e;
    }
    return cipherText;
}
