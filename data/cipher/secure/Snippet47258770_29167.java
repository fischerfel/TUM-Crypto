import java.io.PrintStream;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
public class Decryptno
{
Cipher cipher;
static final String strPassword = "pwd1234";
public Decryptno() {}
static SecretKeySpec key = new SecretKeySpec("pwd1234".getBytes(), "AES");
public static void main(String[] paramArrayOfString) throws Exception {
String str1 = paramArrayOfString[0];
String str2 = null;
IvParameterSpec localIvParameterSpec = new IvParameterSpec("pwd1234".getBytes());

Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

localCipher.init(2, key, localIvParameterSpec);
try {
  if ((str1 != null) && (str1.length() > 0)) {
    byte[] arrayOfByte1 = new BASE64Decoder().decodeBuffer(str1);

    byte[] arrayOfByte2 = localCipher.doFinal(arrayOfByte1);
    str2 = new String(arrayOfByte2);
  } else {
    str2 = str1;
  }
  System.out.println(str2);
} catch (Exception localException) {
  localException.printStackTrace();
}
}
}
