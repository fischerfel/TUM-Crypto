private HttpClient sslClient(HttpClient client) {
        try {
            CustomX509TrustManager tm = new CustomX509TrustManager();
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new MySSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = client.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 8080));
            return new DefaultHttpClient(ccm, client.getParams());
        } catch (Exception ex) {
            return null;
        }
    }
