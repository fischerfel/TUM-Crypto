public  OkHttpClient.Builder getCertificate(OkHttpClient.Builder client) {
    try {
        AssetManager assets = cntxt.getAssets();
        InputStream caInput=null;
        caInput = assets.open(configuration.sslCertFile);

        File f = createFileFromInputStream(caInput);
        final KeyStore trusted = KeyStore.getInstance("PKCS12");
        trusted.load(new FileInputStream(f), sslPasswd.toCharArray());
        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(trusted,sslPasswd.toCharArray());
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        FakeX509TrustManager[] fmk=new FakeX509TrustManager[1];
        fmk[0]=new FakeX509TrustManager();
        fmk[0].allowAllSSL();
        sslContext.init(keyManagerFactory.getKeyManagers(),fmk, new SecureRandom());
        X509TrustManager trustManager = (X509TrustManager) fmk[0];

        client.sslSocketFactory(new Tls12SocketFactory(sslContext.getSocketFactory()), trustManager);
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //Log.d("MAinActivity", "Trust Host :" + hostname);
                return true;
            }
        };
        client.hostnameVerifier( hostnameVerifier);
        ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .build();

        List<ConnectionSpec> specs = new ArrayList<ConnectionSpec>();
        specs.add(cs);
        specs.add(ConnectionSpec.COMPATIBLE_TLS);
        specs.add(ConnectionSpec.CLEARTEXT);

        client.connectionSpecs(specs);
        f.delete();
        log.debug("Certificate File has been deleted from the cache");
    } catch (Exception exc) {
        exc.printStacktrace();
    }

    return client;
}
