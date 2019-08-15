    HTTPConduit httpConduit = (HTTPConduit) ClientProxy.getClient(port).getConduit();

    String keyStoreLoc = "certificates/***-keystore.ks";
    String keyPassword = "*****";

    final KeyStoreParameters ksp = new KeyStoreParameters();
    ksp.setResource(keyStoreLoc);
    ksp.setPassword(keyPassword);

    final KeyManagersParameters kmp = new KeyManagersParameters();
    kmp.setKeyStore(ksp);
    kmp.setKeyPassword(keyPassword);

    TLSClientParameters tlsCP = new TLSClientParameters();
    tlsCP.setKeyManagers(kmp.createKeyManagers());
    tlsCP.setDisableCNCheck(true);
    tlsCP.setUseHttpsURLConnectionDefaultHostnameVerifier(false);
    tlsCP.setUseHttpsURLConnectionDefaultSslSocketFactory(true);
    tlsCP.setHostnameVerifier(new HostnameVerifier()
    {
        public boolean verify(String hostname, SSLSession session)
        {
            return true;
        }
    });

    // Creating Trust Manager
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
    {
        public java.security.cert.X509Certificate[] getAcceptedIssuers()
        {
            return new java.security.cert.X509Certificate[0];
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
        {
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
        {
        }
    } };

    tlsCP.setTrustManagers(trustAllCerts);

    httpConduit.setTlsClientParameters(tlsCP);
