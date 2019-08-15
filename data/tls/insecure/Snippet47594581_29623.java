    private void retrofitConfiguration(Context context){
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            ProviderInstaller.installIfNeeded(context);
            X509Certificate ca = loadCertificate();
            KeyStore keyStore = createKeyStore(ca);
            TrustManager[] trustManagers = createTrustManager(keyStore);
            SSLContext sslContext = null;
            sslContext = createSSLContext(trustManagers, keyStore);
            X509TrustManager _trustManager = (X509TrustManager) trustManagers[0];
            client.sslSocketFactory(sslContext.getSocketFactory(), _trustManager);
            client.hostnameVerifier(getHostnameVerifier());
            buildRetrofit(client.build());
     }

    private X509Certificate loadCertificate() throws CertificateException {
         CertificateFactory cf = CertificateFactory.getInstance("X.509");
         String ca = ROOT_CA;
         byte[] decoded = Base64.decode(ca);
         return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(decoded));
     }

    private KeyStore createKeyStore(X509Certificate ca) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
         String keyStoreType = KeyStore.getDefaultType();
         KeyStore keyStore = KeyStore.getInstance(keyStoreType);
         keyStore.load(null, null);
         keyStore.setCertificateEntry("ca", ca);
         return keyStore;
    }

     private TrustManager[] createTrustManager(KeyStore keyStore) throws NoSuchAlgorithmException, KeyStoreException {
         String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
         TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
         tmf.init(keyStore);
         return tmf.getTrustManagers();
     }

     private SSLContext createSSLContext(TrustManager[] trustManagers, KeyStore keyStore) throws KeyManagementException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
         SSLContext sslContext = SSLContext.getInstance("TLS");

         KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
         kmf.init(keyStore, new char[]{0});
         KeyManager[] keyManagers = kmf.getKeyManagers();

         sslContext.init(keyManagers, trustManagers, null);
         return sslContext;
     }


     private HostnameVerifier getHostnameVerifier() {
         return (hostname, session) -> {
             return true;
         };
     }
