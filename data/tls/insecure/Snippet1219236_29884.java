public class MyManager implements com.sun.net.ssl.X509TrustManager {
  public boolean isClientTrusted(X509Certificate[] chain) { return true; }
  public boolean isHostTrusted(X509Certificate[] chain) { return true; }
  ...
}


com.sun.net.ssl.TrustManager[] managers =
  new com.sun.net.ssl.TrustManager[] {new MyManager()};

com.sun.net.ssl.SSLContext.getInstance("SSL").
       .init(null, managers, new SecureRandom());
