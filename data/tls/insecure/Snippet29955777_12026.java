            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            String algo = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf =TrustManagerFactory.getInstance(algo);
            tmf.init(ks);
            TrustManager managers[]= tmf.getTrustManagers();
            X509TrustManager defaultTrustMgr = null;

            for (int i = 0; i < managers.length; i++) {
                if (managers[i] instanceof X509TrustManager) {
                    defaultTrustMgr = (X509TrustManager) managers[i];
                }
            }
            X509Certificate[] x509Certificates = defaultTrustMgr.getAcceptedIssuers();
            sslContext.init(null, managers, null);
