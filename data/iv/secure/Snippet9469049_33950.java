import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;


public class AES
{
  public byte[] encrypted;
  public byte[] original;

  public String originalString,ske;

  Cipher cipher;
  SecretKeySpec skeySpec;
  static IvParameterSpec spec;
  byte [] iv;
  /*public static String asHex (byte buf[])
  {
    StringBuffer strbuf = new StringBuffer(buf.length * 2);
    int i;
    for (i = 0; i < buf.length; i++) {
    if (((int) buf[i] & 0xff) < 0x10)
    strbuf.append("0");
    strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
  }
  return strbuf.toString();
}*/
  public AES()
  {
        try
        {
            String key ="chetan";
            skeySpec = new SecretKeySpec(getMD5(key),"AES"); 
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        }
        catch(Exception ex)
        {ex.printStackTrace();}
  }
   private static byte[] getMD5(String input){
        try{
            byte[] bytesOfMessage = input.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(bytesOfMessage);
        }  catch (Exception e){
             return null;
        }
    }

public String AESencryptalgo(byte[] text)
{
    String newtext="";
    try
    {
               cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            AlgorithmParameters param = cipher.getParameters();
            IvParameterSpec ivspec=param.getParameterSpec(IvParameterSpec.class);
            iv=ivspec.getIV();
            spec=new IvParameterSpec(iv);
        //AlgorithmParameters params = cipher.getParameters();
        //iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        encrypted = cipher.doFinal(text);

    }
   catch(Exception e)
   {
       e.printStackTrace();
   }
   finally
   {
      newtext=new String(encrypted);
       //System.out.println("ENCRYPTED "+newtext);
       return newtext;
    }
}
public  String AESdecryptalgo(byte[] text)
{
    try
    {

        cipher.init(Cipher.DECRYPT_MODE, skeySpec ,spec);
        original = cipher.doFinal(text);   //Exception occurs here
        originalString = new String(original);
        return originalString;

    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    finally
    {

        return originalString;
    }
}
