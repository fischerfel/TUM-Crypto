KeyStore ks = KeyStore.getInstance("JKS")
FileInputStream fis = new FileInputStream("Path/to/cacerts");

ks.load("fis", "password".toCharArray());

KeyManagerFactory kmfactory=KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmfactory.init(keystore, "password".toCharArray());

TrustMangaerFactory tmf = TrustMangaerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
tmf.init(ks);

SSLContext sc = SSLContext.getInstance("TLS");
sc.init(kmfactory.getKeyManager(), tmf.getKeyManager(),null);
