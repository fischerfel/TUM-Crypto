    ConnectionConfiguration connectionConfig = new ConnectionConfiguration(XMPP_HOSTNAME, 5222);
    connectionConfig.setDebuggerEnabled(true);

    SSLContext sslContext = null;
    try {
        sslContext = SSLContext.getInstance("TLS");
        TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        sslContext.init(null, new TrustManager[] {tm}, null);
        connectionConfig.setCustomSSLContext(sslContext);

        this.connection = new XMPPTCPConnection(connectionConfig);
        this.connection.connect();
        this.connection.login(format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("connecting failed", e);
    }
