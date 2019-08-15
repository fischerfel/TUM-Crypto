        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);tmf.init(keystore);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");

        kmf.init(keystore, "PASSWORD".toCharArray());

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };
        final SSLContext sc = SSLContext.getInstance("TLSv1.2");
        sc.init(kmf.getKeyManagers(), trustAllCerts, new java.security.SecureRandom());
        final SSLSocketFactory socketFactory = sc.getSocketFactory();
        HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
        urlConnection.setSSLSocketFactory(sc.getSocketFactory());
