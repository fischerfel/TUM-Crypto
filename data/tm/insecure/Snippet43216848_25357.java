HttpClient base = new DefaultHttpClient();
SSLContext ctx = SSLContext.getInstance("TLSv1.2");
X509TrustManager tm = new X509TrustManager() {
    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }
    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
 };
 X509HostnameVerifier verifier = new X509HostnameVerifier() {
     @Override
     public void verify(String string, X509Certificate xc) throws SSLException {
     }
     @Override
     public void verify(String string, String[] strings, String[] strings1) throws SSLException {
     }
     @Override
     public boolean verify(String string, SSLSession ssls) {
         return true;
     }
     @Override
     public void verify(String arg0, SSLSocket arg1) throws IOException {
     }
 };
 ctx.init(null, new TrustManager[]{tm}, null);
 SSLSocketFactory ssf = new 
 SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
 ssf.setHostnameVerifier(verifier);
 ClientConnectionManager ccm = base.getConnectionManager();
 SchemeRegistry sr = ccm.getSchemeRegistry();
 sr.register(new Scheme("https", ssf, 443));
 httpClient = new DefaultHttpClient(ccm, base.getParams());
