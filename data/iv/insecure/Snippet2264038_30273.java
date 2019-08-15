public static final byte[] IV = new byte[]
{ 0x04, 0x08, 0x15, 0x16, 0x23, 0x42, 0x00, 0x00, 0x00, 0x00,0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
protected final IvParameterSpec params = new IvParameterSpec(IV);
protected Cipher myCipher;

public AESEncryptor(String passwd, InputStream source, String destinationFile)
{
    try
    {           
        myCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Log.d("System.out.println", "Block Size: "+myCipher.getBlockSize());
        myCipher.init(Cipher.ENCRYPT_MODE, AESEncryptor.generateSecretKeyFromPassword(passwd),params);
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
}
