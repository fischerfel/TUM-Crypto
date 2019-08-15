        DefaultHttpClient httpclient = new DefaultHttpClient();

    SSLContext sslContext;
    try {
        sslContext = SSLContext.getInstance("SSL");

        // set up a TrustManager that trusts everything
        try {
            sslContext.init(null,
                    new TrustManager[] { new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            log.debug("getAcceptedIssuers =============");
                            return null;
                        }

                        public void checkClientTrusted(
                                X509Certificate[] certs, String authType) {
                            log.debug("checkClientTrusted =============");
                        }

                        public void checkServerTrusted(
                                X509Certificate[] certs, String authType) {
                            log.debug("checkServerTrusted =============");
                        }
                    } }, new SecureRandom());
        } catch (KeyManagementException e) {
        }
         SSLSocketFactory ssf = new SSLSocketFactory(sslContext,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
         ClientConnectionManager ccm = this.httpclient.getConnectionManager();
         SchemeRegistry sr = ccm.getSchemeRegistry();
         sr.register(new Scheme("https", 443, ssf));            
    } catch (Exception e) {
        log.error(e.getMessage(),e);
    }
