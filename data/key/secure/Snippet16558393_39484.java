public class Crypto
{ 

  private static byte[] key = null;

  public void setKey(String key){this.key=key.getBytes();}

  public String encrypt(String strToEncrypt)
  {
    String encryptedString =null;
    try
    {
      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
      final SecretKeySpec secretKey = new SecretKeySpec(key,"AES");
      System.out.println("sdfsdf = "+key.toString());
      IvParameterSpec ips = new IvParameterSpec(key);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey,ips);
      encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
    }
    catch(Exception e)
    {
      System.out.println(" ERROR : "+e.getMessage());
    }
    return encryptedString;

  } other method omitted ....
