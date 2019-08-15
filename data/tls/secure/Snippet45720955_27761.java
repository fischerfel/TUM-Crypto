SSLContext sslContext = null;
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream("identity.jsk"),
                "Password".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, "Password".toCharArray());
WEBLOGIC overrides the certificat loaded from Java client and SSL connectiion fails to webservice


    KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
    ts.load(new FileInputStream("certi.jk"),
            "Password".toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ts);

    sslContext = SSLContext.getInstance("TLSv1.2");
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
