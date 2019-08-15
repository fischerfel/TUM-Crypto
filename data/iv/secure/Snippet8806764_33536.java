public byte[] readMessage () throws Exception
{
    byte[] iv = new byte[16];
    byte[] message = new byte[EncryptionSettings.encryptionMessageLength];

    try
    {
        // read IV from stream
        if (stream.read(iv) != 16)
            throw new Exception("Problem receiving full IV from stream");
    }
    catch (final IOException e)
    {
        throw new Exception("Unable to read IV from stream");
    }

    try
    {
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
    }
    catch (final InvalidKeyException e)
    {
        throw new Exception("Invalid key");
    }
    catch (final InvalidAlgorithmParameterException e)
    {
        throw new Exception("Invalid algorithm parameter");
    }

    try
    {
        //read message from stream
        if (stream.read(message) != EncryptionSettings.encryptionMessageLength)
             throw new Exception("Problem receiving full encrypted message from stream");
    }
    catch (final IOException e)
    {
        throw new Exception("Unable to read message from stream");
    }

    try
    {
        return cipher.doFinal(message); //decipher message and return it.
    }
    catch (IllegalBlockSizeException e)
    {
        throw new Exception("Unable to decrypt message due to illegal block size - "
                          + e.getMessage());
    }
    catch (BadPaddingException e)
    {
        throw new Exception("Unable to decrypt message due to bad padding - "
                            + e.getMessage());
    }
}
