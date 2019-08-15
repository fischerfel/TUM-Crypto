import java.math.BigInteger; 
import java.security.KeyFactory; 
import java.security.interfaces.RSAPublicKey; 
import java.security.spec.*; 
import javax.crypto.Cipher;

public class OxiSecurity {

public String encryption(String text)
{
    byte[] bb={},cc=new byte[128];
    String s1=null;
    String s2=null;
    byte[] cipherData={} ;
      try
      {
          BigInteger modulus = new BigInteger("C60ADE82F8922A025ED9BBD02E8D6C0AAEBA2F387E9E83D1A0A530E7E7FF8A6B7F4C86233AFEFB97C3F606D6CD76B4A3BAF3F93AE79C16E3FB764C1DCBB73744A5C2C2F3ED878FF5181A558A8917CA1164BFE0A088F13859FA22D1A48362051407523E0E11AC90E18FC4CBFD70DBC2149EF62316DC063C647A3319E96B7727EB",16);
          BigInteger pubExp = new BigInteger("65537");
          KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
          RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, pubExp);
          RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
          System.out.print(key);
          Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
          cipher.init(Cipher.ENCRYPT_MODE, key);
          cipherData = cipher.doFinal(text.getBytes());   
          char[] c=new char[128];
             for(int i=0;i<128;i++)
             {
                 if(cipherData[i]<0)
                     c[i]=(char)(cipherData[i]+256);
                 else
                     c[i]=(char)cipherData[i];
             }
            s2= new String(String.copyValueOf(c)); 
            char[] my = s2.toCharArray();

             for(int i=0;i<128;i++)
             {
                 if((int)my[i]>0)
                     cc[i]=(byte)(my[i]-256);
                 else
                     cc[i]=(byte)my[i];
             }

          s1 = new String(cipherData);
         System.out.print(s1);
         bb=s1.getBytes();
          //String s=s1;
         String s = new String(cipherData, "UTF8");
          return s2;
      }
      catch(Exception e)
      {
          e.printStackTrace();
      }   
      finally
      {

          return s2;
      }
}

}
