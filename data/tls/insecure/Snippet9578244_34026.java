SSLContext ctx = SSLContext.getInstance("TLS");
X509TrustManager tm = new X509TrustManager() {

    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
};
ctx.init(null, new TrustManager[]{tm}, null);
SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
ClientConnectionManager ccm = base.getConnectionManager();
SchemeRegistry sr = ccm.getSchemeRegistry();
sr.register(new Scheme("https", 443, ssf));

client = new DefaultHttpClient(ccm, base.getParams());
