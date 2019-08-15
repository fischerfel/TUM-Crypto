private TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            java.security.cert.X509Certificate[] chck = null;
            return chck;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }
    } };


sslConfig = SslConfigurator.newInstance().trustStoreFile(trustStoreFile).trustStorePassword(trustStorePassword);
        // sslContext = sslConfig.createSSLContext();
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;

        basicAuth = HttpAuthenticationFeature.basic(username, userPassword);
        clientConfig = new DefaultClientConfig();
        JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        clientConfig.getSingletons().add(jacksonJsonProvider);
        client = Client.create(clientConfig);
        client.addFilter(new HTTPBasicAuthFilter(username, userPassword));
        webResource = client.resource(uri);
