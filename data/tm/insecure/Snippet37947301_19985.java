private void disableCertificateVerification() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)                  throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                    throws CertificateException {
            }
        } };

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            HttpsURLConnection.setDefaultHostnameVerifier(
                    (hostname, session) -> hostname.equals("IPADDRESS"));
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOGGER.error(e.toString());
        }
    }
