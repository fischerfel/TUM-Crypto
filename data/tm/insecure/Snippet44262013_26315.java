private SSLSocketFactory getFakeSSLContext() {
        final SSLContext sslContext;

        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        }};

        try {
            sslContext = SSLContext.getInstance("TLS");

            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create all-trusting host name verifier
            final HostnameVerifier allHostsValid = (hostname, session) -> true;
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            return sslContext.getSocketFactory();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            LogHelper.e("error creating fake ssl context", e);
        }
        return null;
    }
