    char [] KSPASS = "password".toCharArray();
    char [] KEYPASS = "password".toCharArray();
    try {
        final KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(context.getResources().openRawResource(R.raw.serverkeys), KSPASS);

        final KeyManagerFactory keyManager = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManager.init(keyStore, KEYPASS);

        final TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustFactory.init(keyStore);

        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManager.getKeyManagers(), trustFactory.getTrustManagers(), null);
        Arrays.fill(KSPASS, ' ');
        Arrays.fill(KEYPASS, ' ');

        KSPASS = null;
        KEYPASS = null;
