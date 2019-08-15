     import java.io.*;
     import java.util.*;
     import javax.crypto.*;
     import javax.crypto.spec.*;

     import java.security.Provider;
     import java.security.Security;
     public class Test
     {
       public static byte[] raw =null;
       public static SecretKeySpec skeySpec;
       public static Cipher cipher;
       public static void main(String ags[]) throws Exception
       {
           byte[] key={1,2,3,4,5,6,7};
           skeySpec = new SecretKeySpec(key, "Blowfish");
           System.out.println("KEY : "+bytesToString(skeySpec.getEncoded()));
                   String cipherInstName = "Blowfish/ECB/PKCS5Padding";
           cipher = Cipher.getInstance(cipherInstName);
           cipher.init(Cipher.ENCRYPT_MODE,skeySpec);
           byte[] encrypted = cipher.doFinal(("asdfgh").getBytes());        
           System.out.println("PLAIN TEXT : "+("asdfgh").getBytes());
           System.out.println("ENCRYPTED TEXT : "+bytesToString(encrypted));             
       }

           private static String bytesToString(byte [] value)
           {
                   StringBuffer retVal = new StringBuffer();
                   for(int i=0; i<value.length; i++)
                   {
                       retVal.append(value[i]+":");
                   }
                   int inx = retVal.toString().lastIndexOf(":");
                   retVal= new StringBuffer(retVal.toString().substring(0,inx));
                   return retVal.toString();
           }
     }
