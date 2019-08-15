public class HttpProvider implements INetworkProvider {
  private static final String LOG = "HttpProvider";
  protected DefaultHttpClient client;

  public HttpProvider() {
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
    sslSocketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    registry.register(new Scheme("https", sslSocketFactory, 443));

    client = new DefaultHttpClient(
      new ThreadSafeClientConnManager((new BasicHttpParams()), registry), new BasicHttpParams()
    );
  }
[snip]
