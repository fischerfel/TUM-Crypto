public static void main(String[] arg) throws Exception {
    String url = "https://google.com";

    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(null, new TrustManager[] { new TrustManager() }, null);
    SSLContext.setDefault(ctx);

    HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
    conn.setHostnameVerifier(new HostVerifier());
    conn.setRequestMethod("GET");
    conn.connect();
    System.out.println("Response: " + conn.getResponseCode());
}

private static class HostVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String paramString, SSLSession paramSSLSession) {
     return true;
    }
}

private static class TrustManager implements X509TrustManager {

    @Override
    public X509Certificate[] getAcceptedIssuers() {
    return null;
    }

    @Override
    public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
        throws CertificateException {

    }

    @Override
    public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
        throws CertificateException {
    }
}
