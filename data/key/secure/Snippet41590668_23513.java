import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.crypto.Cipher;
import java.util.Base64;
import java.security.MessageDigest;

public class HelloWorld
{
  public static void main(String[] args) {
    String keyhex,salt,salto,keystr;
    salt = "mysaltmysalt1234";
    keyhex = "ce4f5bf05a57b6f9e0d7df628d266d66";
    byte[] binkey = DatatypeConverter.parseHexBinary(keyhex);
    keystr = new String(binkey);
    salto = encrypt(salt.getBytes(), keystr);
    System.out.println(salto);
  }



  public static String encrypt(byte[] key,String inputString) {
    try
    {
      SecretKeySpec secretKeySpec = new SecretKeySpec(key,
 "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(1, secretKeySpec);
      byte[] aBytes = cipher.doFinal(inputString.getBytes());
      String base64 = new String(Base64.getEncoder().encode(aBytes));
      return base64; 
    } catch(Exception ex) {
        System.out.println("Exception occured in encrypt : "+ex.toString());
    }
    return null; 
  }

}
