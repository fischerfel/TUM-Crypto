package Encrypt;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import org.apache.commons.codec.binary.Base64;
import org.apache.hadoop.hive.ql.exec.FunctionTask;
import java.security.MessageDigest;

import javax.crypto.Cipher;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class Dec1 extends UDF {

public Text evaluate(final Text s) {
  if (s == null) {
    return null;
   }
  byte[] sharedvector = {
   0x01, 0x02, 0x03, 0x05, 0x07, 0x0B, 0x0D, 0x11
   };

String RawText = "";
byte[] keyArray = new byte[24];
byte[] temporaryKey;
String key = "developersnotedotcom";
byte[] toEncryptArray = null;

try
  {

    MessageDigest m = MessageDigest.getInstance("MD5");
        temporaryKey = m.digest(key.getBytes("UTF-8"));           

        if(temporaryKey.length < 24) // DESede require 24 byte length key
        {
            int index = 0;
            for(int i=temporaryKey.length;i< 24;i++)
            {                  
                keyArray[i] =  temporaryKey[index];
            }
        }

        Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));
        byte[] decrypted = c.doFinal(Base64.decodeBase64(s.toString()));    
        RawText = new String(decrypted, "UTF-8"); 
   }
   catch(Exception NoEx)
    {
    //JOptionPane.showMessageDialog(null, NoEx);
     System.out.println(NoEx + "This is Udf error");
     System.exit(1);
    }

   return new Text(RawText.toString());        
}

}
