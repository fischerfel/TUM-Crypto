public void trustSelfSignedSSL() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] xcs,
                        String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs,
                        String string) throws CertificateException {
                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws java.security.cert.CertificateException {

                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLContext.setDefault(ctx);
        } catch (Exception ex) {
            throw new RuntimeException("Exception occurred ",ex)
        }
    }
