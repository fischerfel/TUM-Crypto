public static SSLContext createSSLContext(Certificate ca, PrivateKey key, Certificate[] chain)
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        char[] pwd = new char[1];
        keyStore.load(null, pwd);
        if (key != null && chain != null) {
            keyStore.setKeyEntry("key", key, pwd, chain);
        }
        if (ca != null) {
            keyStore.setCertificateEntry("cert", ca);
        }

        SSLContext sc = SSLContext.getInstance("SSL");

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, pwd);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        sc.init(key != null ? keyManagerFactory.getKeyManagers() : null,
                ca != null ? trustManagerFactory.getTrustManagers() : null,
                null);
        return sc;
}
