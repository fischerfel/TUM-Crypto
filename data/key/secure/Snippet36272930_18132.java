private byte[] crypt(int mode, byte[] key, byte[] initializationVector, byte[] data)
{
    try
    {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVector);

        cipher.init(mode, secretKeySpec, ivParameterSpec);

        return cipher.doFinal(data);
    }
    catch(BadPaddingException | InvalidKeyException | IllegalBlockSizeException | InvalidAlgorithmParameterException e)
    {
        throw new RuntimeException(e);
    }
}
