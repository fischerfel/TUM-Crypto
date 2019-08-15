public class TrustAllX509Manager implements X509TrustManager
{
  @Override
  public X509Certificate[] getAcceptedIssuers()
  {
    return null;
  }

  @Override
  public void checkClientTrusted(X509Certificate[] certs, String authType)
  {
    // nothing
  }

  @Override
  public void checkServerTrusted(X509Certificate[] certs, String authType)
  {
    // nothing
  }
}
