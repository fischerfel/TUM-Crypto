import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public final class c
{

private Cipher a;
private Cipher b;
private final byte c[] = {
    -87, -101, -56, 50, 86, 53, -29, 3
};

public c(String s)
{
    PBEKeySpec pbekeyspec = new PBEKeySpec(s.toCharArray(), c, 20);
    try
    {
        SecretKey secretkey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(pbekeyspec);
        a = Cipher.getInstance(secretkey.getAlgorithm());
        b = Cipher.getInstance(secretkey.getAlgorithm());
        PBEParameterSpec pbeparameterspec = new PBEParameterSpec(c, 20);
        a.init(1, secretkey, pbeparameterspec);
        b.init(2, secretkey, pbeparameterspec);
        return;
    }
    catch (Exception exception)
    {
        return;
    }
  }

  public final String a(String s)
  {
    String s1;
    try
    {
        s1 = Base64.encodeToString(a.doFinal(s.getBytes()), 0);
    }
    catch (Exception exception)
    {
        return null;
    }
    return s1;
  }
}
