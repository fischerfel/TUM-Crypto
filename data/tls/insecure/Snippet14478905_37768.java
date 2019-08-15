    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(this.getClass().getResourceAsStream("keystore.jks"), "haslo1".toCharArray());
    TrustManagerFactory trustManagerFactory =
    TrustManagerFactory.getInstance("PKIX", "SunJSSE");
    trustManagerFactory.init(keyStore);
    X509TrustManager x509TrustManager = null;
    for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
    if (trustManager instanceof X509TrustManager) {
    x509TrustManager = (X509TrustManager) trustManager;
    break;
    }
    }
        if (x509TrustManager == null) {
            throw new NullPointerException();
        }
        SSLContext sslContext = SSLContext.getInstance("TLS");

      sslContext.init( KeyManagerFactory.getInstance( "RSA" ).getKeyManagers(),
       new TrustManager[]{x509TrustManager}, null);
