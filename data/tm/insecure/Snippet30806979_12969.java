public class EasyTrustManager implements X509TrustManager {
  public void checkClientTrusted(X509Certificate[] chain, String authType) {
        //do nothing
  }

  public void checkServerTrusted(X509Certificate[] chain, String authType) {
       //do nothing
  }

  public java.security.cert.X509Certificate[] getAcceptedIssuers() {
       return null;
  }
}
