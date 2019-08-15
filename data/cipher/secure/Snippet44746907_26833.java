import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Lc {
    public static PublicKey pub;
      public static PrivateKey pri;
      public byte[] by;
      public String dot;
      public Lc() {
          Object localObject = "RSA";
          try
          {
            localObject = KeyPairGenerator.getInstance((String)localObject);
            KeyPair localKeyPair = ((KeyPairGenerator)localObject).generateKeyPair();
            localObject = localKeyPair.getPublic();
            pub = (PublicKey)localObject;
            localObject = localKeyPair.getPrivate();
            pri = (PrivateKey)localObject;
          }
          catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
          {
            localNoSuchAlgorithmException.printStackTrace();
          }
    }
      public static String upperDot(String paramString)
      {
        Object localObject = "RSA/ECB/PKCS1Padding";
        try
        {
          Cipher localCipher = Cipher.getInstance((String)localObject);
          localObject = pub;
          int i = 1;
          localCipher.init(i, (Key)localObject);
          localObject = paramString.getBytes();
          byte[] arrayOfByte1 = localCipher.doFinal((byte[])localObject);
          byte[] arrayOfByte2 = Base64.encodeBase64(arrayOfByte1);
          localObject = new String(arrayOfByte2);
          return (String)localObject;
        }
        catch (Exception localException)
        {
          System.out.print(localException);
        }
        return null;
      }
}
