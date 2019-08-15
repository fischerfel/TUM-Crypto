import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class MyArcFour
{
   public static void main(String [] args) throws Exception
   {
      byte [] key = "MYVERYINSECUREKEY".getBytes("ASCII");

      String clearText = "123456789012";

      Cipher rc4 = Cipher.getInstance("RC4");
      SecretKeySpec rc4Key = new SecretKeySpec(key, "RC4");
      rc4.init(Cipher.ENCRYPT_MODE, rc4Key);

      byte [] cipherText = rc4.update(clearText.getBytes("ASCII"));

      System.out.println("clear (ascii)        " + clearText);
      System.out.println("clear (hex)          " + DatatypeConverter.printHexBinary(clearText.getBytes("ASCII")));
      System.out.println("cipher (hex) is      " + DatatypeConverter.printHexBinary(cipherText));

      Cipher rc4Decrypt = Cipher.getInstance("RC4");
      rc4Decrypt.init(Cipher.DECRYPT_MODE, rc4Key);
      byte [] clearText2 = rc4Decrypt.update(cipherText);

      System.out.println("decrypted (clear) is " + new String(clearText2, "ASCII"));
   }
}
