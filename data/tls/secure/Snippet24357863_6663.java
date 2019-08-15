        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, null, null);
        String[] protocols = sslContext.getSupportedSSLParameters().getProtocols();
        for (String protocol : protocols) {
            Timber.d("Context supported protocol: " + protocol);
        }

        SSLEngine engine = sslContext.createSSLEngine();
        String[] supportedProtocols = engine.getSupportedProtocols();
        for (String protocol : supportedProtocols) {
            Timber.d("Engine supported protocol: " + protocol);
        }
