public final class PasswordEncryption
{
  private static PasswordEncryption instance;
  private PasswordEncryption() 
  {
  }
  public synchronized String encrypt(String plaintext) throws UnavailableException
  {
    MessageDigest md = null;
    try
    {
      md = MessageDigest.getInstance("SHA-256"); //step 2
    }
    catch(NoSuchAlgorithmException e)
    {
      System.out.println("NoSuchAlgorithmException");

    }
    try
    {
      md.update(plaintext.getBytes("UTF-8")); //step 3
    }
    catch(UnsupportedEncodingException e)
    {
      throw new UnavailableException(e.getMessage());
    }
    byte raw[] = md.digest(); //step 4
    String hash = (new BASE64Encoder()).encode(raw); //step 5
    return hash; //step 6
  }
  public static synchronized PasswordEncryption getInstance() //step 1
  {
    if(instance == null)
    {
      return new PasswordEncryption();
    }
    else    
    {
      return instance;
    }
  }
}
