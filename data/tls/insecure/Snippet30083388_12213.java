        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore)null); //this is where you would add the truststore
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("PKCS12", BouncyCastleProvider.PROVIDER_NAME); //spongyCastle library
        keyStore.load(keyStoreStream, keyStorePassword); //inputStream to PKCS12
        keyManagerFactory.init(keyStore, keyStorePassword);
        //TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        TrustManager[] trustAllCertManagers = { new X509TrustManager() { // this is vulnerable to MITM attack
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};

        sslContext.init(keyManagerFactory.getKeyManagers(), trustAllCertManagers, new SecureRandom());
        URL url = new URL(urlString);
        HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) url.openConnection();
        httpsUrlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
