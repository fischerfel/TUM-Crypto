
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.Provider;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;

public class Test01
{
 private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

 public static void main (String [] arguments)
 {
  byte[] key = { 1,2,3,4,5,6,7,8};
  SecretKeySpec SHA1key = new SecretKeySpec(key, "HmacSHA1");
  Mac hmac;
  String strFinalRslt = "";

  try {
 hmac = Mac.getInstance("HmacSHA1");
 hmac.init(SHA1key);
 byte[] result = hmac.doFinal();
    strFinalRslt = toHexString(result);

  }catch (NoSuchAlgorithmException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
  }catch (InvalidKeyException e) {
 // TODO Auto-generated catch block
 e.printStackTrace();
  }catch(StackOverflowError e){
 e.printStackTrace();
  }
  System.out.println(strFinalRslt);
  System.out.println("All done!!!");
 }

 public static byte[] fromHexString ( String s )
 {
  int stringLength = s.length();
  if ( (stringLength & 0x1) != 0 )
  {
   throw new IllegalArgumentException ( "fromHexString requires an even number of hex characters" );
  }
  byte[] b = new byte[stringLength / 2];

  for ( int i=0,j=0; i>> 4] );

  //look up low nibble char
  sb.append( hexChar [b[i] & 0x0f] );
 }
 return sb.toString();
}

static char[] hexChar = {
'0' , '1' , '2' , '3' ,
'4' , '5' , '6' , '7' ,
'8' , '9' , 'a' , 'b' ,
'c' , 'd' , 'e' , 'f'};


}

