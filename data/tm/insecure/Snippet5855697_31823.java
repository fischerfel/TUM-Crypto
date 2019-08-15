public class NoVerifyTM implements X509TrustManager
{
  void checkClientTrusted(X509Certificate[] chain, String authType) {
    /* Accept All */
  }

  void checkServerTrusted(X509Certificate[] chain, String authType) {
    /* Accept All */
  }

  X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[0]
  }
}
