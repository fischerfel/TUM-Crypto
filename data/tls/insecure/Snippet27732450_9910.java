Builder options = MongoClientOptions.builder();

KeyStore ks = KeyStore.getInstance("JKS");
ks.load(new FileInputStream("path/to/keystore"), "pass".toCharArray());

TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
trustFactory.init(ks);

SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, trustFactory.getTrustManagers(), null);

options.socketFactory(sc.getSocketFactory());
new MongoClient("loclahost", options.build());
