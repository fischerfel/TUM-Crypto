import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import tw.com.januarytc.android.singularsdk.lib.JsLib;
import android.util.Log;

public class DESUtil
{
  private KeyGenerator keyGen=null;
  private SecretKey sKey=null;
  private Cipher desCip=null;

  /**
   * Init. DES utility class
   * @return boolean
   */
  public boolean init()
  {
    boolean b=false;

    try
    {
      keyGen=KeyGenerator.getInstance("DES");
      sKey=keyGen.generateKey();
      desCip=Cipher.getInstance("DES/ECB/PKCS5Padding");
      b=true;
    }
    catch(Exception e)
    {
      Log.d(JsLib.TAG, "Init DESUtil failed: "+e.toString());
      e.printStackTrace();
      b=false;
    }
    return b;
  }

  /**
   * Encrypt string with DES
   * @param str - Original string
   * @return java.lang.String DES encrypted string
   * @throws IllegalStateException
   */
  public String encryptString(String str) throws IllegalStateException
  {
    if(keyGen==null || sKey==null || desCip==null){throw new IllegalStateException("DESUtil class has not been initialized.");}
    String ret="";
    try
    {
      desCip.init(Cipher.ENCRYPT_MODE, sKey);
      ret=new String(desCip.doFinal(str.getBytes("UTF-8")));
    }
    catch(Exception e)
    {
      e.printStackTrace();
      ret="";
    }
    return ret;
  }

  /**
   * Decrypt string which encrypted by DES
   * @param str - DES encrypted string
   * @return java.lang.String Original string
   * @throws IllegalStateException
   */
  public String decryptString(String strDes) throws IllegalStateException
  {
    if(keyGen==null || sKey==null || desCip==null){throw new IllegalStateException("DESUtil class has not been initialized.");}
    String ret="";
    try
    {
      desCip.init(Cipher.DECRYPT_MODE, sKey);
      ret=new String(desCip.doFinal(strDes.getBytes("UTF-8")));
    }
    catch(Exception e)
    {
      e.printStackTrace();
      ret="";
    }
    return ret;
  }
}
