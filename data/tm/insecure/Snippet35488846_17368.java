import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class AnyTrustManager implements X509TrustManager
{
  X509Certificate[] client = null;
  X509Certificate[] server = null;

  public void checkClientTrusted(X509Certificate[] chain, String authType)
  {
    client = chain;
  }

  public void checkServerTrusted(X509Certificate[] chain, String authType)
  {
    server = chain;
  }

  public X509Certificate[] getAcceptedIssuers()
  {
    return new X509Certificate[0];
  }
}
