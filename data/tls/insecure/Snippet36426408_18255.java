        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, null);

        TrustManager[] tms = WrapTrustManager.WrapArray(tmf.getTrustManagers());
        KeyManager[] kms = WrapKeyManager.WrapArray(kmf.getKeyManagers());
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(kms, tms, null);

        ....setSocketFactory(context.getSocketFactory());
