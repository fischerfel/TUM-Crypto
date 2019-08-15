final KeyStore keyStore = KeyStore.getInstance("BKS");
    keyStore.load(context.getResources().openRawResource(R.raw.serverkeys), null);

    final KeyManagerFactory keyManager = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManager.init(keyStore, null);

    final TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustFactory.init(keyStore);

    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManager.getKeyManagers(), trustFactory.getTrustManagers(), null);
