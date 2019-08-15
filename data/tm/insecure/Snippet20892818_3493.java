public class SslUtil {

private static class SmacsTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}


public static void acceptAllCerts(){
    try{
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], new TrustManager[] {new SmacsTrustManager()}, new SecureRandom());
        SSLContext.setDefault(ctx);

    }catch (Exception e){

    }
  }
}
