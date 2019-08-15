    String targetAddr = http.getTarget().getAddress().getValue();
    if (targetAddr.toLowerCase().startsWith("https:")) {
        TrustManager[] simpleTrustManager = new TrustManager[] { new X509TrustManager() {
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        } };
        TLSClientParameters tlsParams = new TLSClientParameters();
        tlsParams.setTrustManagers(simpleTrustManager);
        tlsParams.setDisableCNCheck(true);
        http.setTlsClientParameters(tlsParams);
    }
