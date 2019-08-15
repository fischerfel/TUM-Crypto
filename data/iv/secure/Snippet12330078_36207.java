/*Constructor*/
public DataCrypt()
{
   ivspec = new IvParameterSpec(iv.getBytes());

   keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

   try
   {
      cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
   }
   catch (NoSuchAlgorithmException e)
   {
                // TODO Auto-generated catch block
                e.printStackTrace();
    }
   catch (NoSuchPaddingException e)
   {
                // TODO Auto-generated catch block
                e.printStackTrace();
   }
}

public byte[] encrypt(String text) throws Exception
{
    if(text == null || text.length() == 0)
       throw new Exception("Empty string");

    byte[] encrypted = null;

    try
    {
      cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

      encrypted = cipher.doFinal(text.getBytes("UTF-8"));
    }
    catch (Exception e)
    {                       
      throw new Exception("[encrypt] " + e.getMessage());
    }      
    return encrypted;
}
