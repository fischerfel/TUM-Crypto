import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.PrintStream;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SecureCardData
{
  public static final String retailerid = "61220121";

  public String encryptData(String sData)
    throws Exception
  {
    byte[] bPrivateKey = "61220121".getBytes();
    SecretKeySpec spec = new SecretKeySpec(bPrivateKey, "DES");
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(1, spec);
    byte[] bEncryptedData = cipher.doFinal(sData.getBytes());
    return Base64.encode(bEncryptedData);
  }

  public String decryptData(String sData)
    throws Exception
  {
    byte[] bPrivateKey = "61220121".getBytes();
    SecretKeySpec spec = new SecretKeySpec(bPrivateKey, "DES");
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(2, spec);
    byte[] bencryptedData = Base64.decode(sData);
    byte[] bDecryptedData = cipher.doFinal(bencryptedData);
    return new String(bDecryptedData);
  }

  public static void main(String[] args)
    throws Exception
  {
    String s = "1800585544448888|445|0611";
    SecureCardData sd = new SecureCardData();
    String ss = sd.encryptData(s);
    System.out.println(ss);

    ss = sd.decryptData(ss);
    System.out.println(ss);
  }
}
