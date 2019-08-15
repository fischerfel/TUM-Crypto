private static Cipher cipher;
private static SecretKeySpec key;

static
{
    try
    {
        key = new SecretKeySpec(NettyServer.getSecretKey(), "AES");
        cipher = Cipher.getInstance("AES/CBC/NoPadding");
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
