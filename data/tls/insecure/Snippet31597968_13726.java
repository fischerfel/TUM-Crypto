SSLContext sc = SSLContext.getInstance("TLS");

KeyManagerFactory kmf = KeyManagerFactory
        .getInstance(KeyManagerFactory.getDefaultAlgorithm());

KeyStore ks = KeyStore.getInstance("JKS");
ks.load(getClass().getResourceAsStream(keystore),
        password.toCharArray());

kmf.init(ks, password.toCharArray());

sc.init(kmf.getKeyManagers(), null, null);

HttpsURLConnection
        .setDefaultSSLSocketFactory(sc.getSocketFactory());

yourService = new YourService(url); //Handshake should succeed
