CloseableHttpClient httpclient = HttpClients.custom()
            .setSSLSocketFactory(sslsf)
            .setConnectionManager(cm)
            .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
            .build();
