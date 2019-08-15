import org.apache.http.conn.ssl.SSLSocketFactory;

public class MyHttpClient extends DefaultHttpClient {
  final Context context;

  public MyHttpClient(Context context) {
    this.context = context;
  }

  @Override 
  protected ClientConnectionManager createClientConnectionManager() {
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    registry.register(new Scheme("https", newSslSocketFactory(), 443));
    return new SingleClientConnManager(getParams(), registry);
  }

  private SSLSocketFactory newSslSocketFactory() {
    try {
      TrustManager tm = new MyCustomTrustManager();
      SSLContext ctx = SSLContext.getInstance("TLS");
      ctx.init(null, new TrustManager[] {tm}, null);
      SSLSocketFactory sf = new SSLSocketFactory(ctx);
      return new SSLSocketFactory(ctx);
    } catch (Exception e) {
      throw new Error(e);
    }
  }
}
