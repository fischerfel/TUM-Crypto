private static HttpClient wrapClient(HttpClient httpClient) {       
    try {
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] xcs,
                    String string) {
            }

            public void checkServerTrusted(X509Certificate[] xcs,
                    String string) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        X509HostnameVerifier verifier = new X509HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return false;
            }

            @Override
            public void verify(String arg0, SSLSocket arg1)
                    throws IOException { }

            @Override
            public void verify(String arg0, X509Certificate arg1)
                    throws SSLException { }

            @Override
            public void verify(String arg0, String[] arg1, String[] arg2)
                    throws SSLException { }

        };

        ctx.init(null, new TrustManager[] { tm }, null);

        SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
        socketFactory.setHostnameVerifier(verifier);
        Scheme sch = new Scheme("https", 443, socketFactory);
        httpClient.getConnectionManager().getSchemeRegistry().register(sch);
        return httpClient;

    } catch (Exception ex) {
        ex.printStackTrace();
        return null;
    }
}
