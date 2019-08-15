    private DefaultHttpClient getSSLHttpClient(final URL url) throws RestClientException {
    try {
        final X509TrustManager trustManager = createTrustManager();
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[]{trustManager}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx, createHostnameVerifier());
        AbstractHttpClient base = new DefaultHttpClient();
        ClientConnectionManager ccm = base.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme(HTTPS, url.getPort(), ssf));
        return new DefaultHttpClient(ccm, base.getParams());
    } catch (final Exception e) {
        throw new RestClientException(FAILED_CREATING_CLIENT, "Failed creating http client",
                ExceptionUtils.getFullStackTrace(e));
    }
}


private X509TrustManager createTrustManager() {
    X509TrustManager tm = new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(final X509Certificate[] chain, final String authType) 
                throws CertificateException {
        }

        @Override
        public void checkClientTrusted(final X509Certificate[] chain, final String authType) 
                throws CertificateException {
        }
    };
    return tm;
}


private X509HostnameVerifier createHostnameVerifier() {
    X509HostnameVerifier verifier = new X509HostnameVerifier() {

        @Override
        public boolean verify(final String arg0, final SSLSession arg1) {
            return true;
        }

        @Override
        public void verify(final String host, final String[] cns, final String[] subjectAlts) 
                throws SSLException {
        }

        @Override
        public void verify(final String host, final X509Certificate cert) 
                throws SSLException {
        }

        @Override
        public void verify(final String host, final SSLSocket ssl) 
                throws IOException {
        }
    };
    return verifier;
}
