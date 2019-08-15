KeyStore ks = KeyStore.getInstance("PKCS12")
FileInputStream fis = new FileInputStream("Path/to/xyz.pfx");

ks.load("fis", "password".toCharArray());

KeyManagerFactory kmfactory=KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmfactory.init(keystore, "password".toCharArray());


SSLContext sc = SSLContext.getInstance("TLS");
sc.init(kmfactory.getKeyManager(), null,null);
