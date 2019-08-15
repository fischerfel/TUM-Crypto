SSLContext sslContext = SSLContext.getInstance("TLS");
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(keyStore, "".toCharArray());

    sslContext.init(kmf.getKeyManagers(), wrappedTrustManagers, null);
