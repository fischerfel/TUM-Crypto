import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


 private static final String ALGORITHM = "AES";
private static final String KEY = "1Hbfh667adfDEJ78";

 String encryptedPassword = null;  String encryptname = null;
              try {
                  encryptedPassword = encrypt(getpassword);
                  encryptname = encrypt(ppword);
              } catch (Exception ex) {
                  Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
              }

public  String encrypt(String value) throws Exception
{
Key key = generateKey();
Cipher cipher = Cipher.getInstance(ALGORITHM);
cipher.init(Cipher.ENCRYPT_MODE, key);
byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
String encryptedValue64 = new BASE64Encoder().encode(encryptedByteValue);
return encryptedValue64;
}

private  Key generateKey() throws Exception
{
Key key = new SecretKeySpec(KEY.getBytes(),ALGORITHM);
return key;
}
