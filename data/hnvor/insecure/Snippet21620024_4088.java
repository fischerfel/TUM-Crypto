public class Test {


public static void main(String [] args) throws Exception {
    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
    SSLContext.setDefault(ctx);

    URL url = new URL("https://www.google.com");
    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    conn.setHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    });
    System.out.println(conn.getResponseCode());
    conn.disconnect();
}

private static class DefaultTrustManager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        //Do certificate verification here and throw exception if invalid
        throw new CertificateException();
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}

}
