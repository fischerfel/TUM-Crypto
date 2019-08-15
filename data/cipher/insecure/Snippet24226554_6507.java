package Encrypt;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import java.security.*;
import org.apache.commons.codec.binary.Base64;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.swing.JOptionPane;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class En1 extends UDF {

public Text evaluate(final Text s) throws Exception {
if (s == null) {
 return null;
}
byte[] sharedvector = {
0x01, 0x02, 0x03, 0x05, 0x07, 0x0B, 0x0D, 0x11
};

String EncText = "";
byte[] keyArray = new byte[24];
byte[] temporaryKey;
String key = "developersnotedotcom";
byte[] toEncryptArray = null;

//try
   // {

    toEncryptArray =  s.toString().getBytes("UTF-8");        
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
    c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));            
    byte[] encrypted = c.doFinal(toEncryptArray);            
    EncText = Base64.encodeBase64String(encrypted);


//  }
   /* catch(NoSuchAlgorithmException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException NoEx)
{
    //JOptionPane.showMessageDialog(null, NoEx);
     System.out.println(NoEx);
     System.exit(1);
}*/

return new Text(EncText.toString());        
}

}
