private static final TrustManager[] TRUST_ALL_CERTS = new TrustManager[]{
        new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }
        }

    };

    private static X509HostnameVerifier ACCEPT_ALL_HOSTNAMES = 
            new X509HostnameVerifier() {

                public void verify(String host, String[] cns, String[] subjectAlts)
                        throws SSLException {
                }

                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                public boolean verify(String host, SSLSession session) {
                    return true;
                }
            };
