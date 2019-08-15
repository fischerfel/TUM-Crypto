        client = new FTPSClient(implictSSL);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(KeyStore.getInstance("BKS"), "wshr.ut".toCharArray());         

        client.setTrustManager(new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
        });

        client.setKeyManager(kmf.getKeyManagers()[0]);
        client.setNeedClientAuth(false);
        client.setUseClientMode(false);

         if(timeout > 0) {
            client.setConnectTimeout(timeout);
            client.setDataTimeout(timeout);
            client.setDefaultTimeout(timeout);
         }

        client.connect(values.host, values.port);
