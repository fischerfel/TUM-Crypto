char [] keyphrase="xxx".toCharArray();
char [] passphrase= "yyy".toCharArray();

// First initialize the key and trust material.
KeyStore ksKeys = KeyStore.getInstance("JKS");
InputStream readStream = new FileInputStream(new File("/.../file.jks"));
ks.load(readStream, passphrase );
// create an factory for key-managers
KeyManagerFactory   =KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ks, keyphrase);
SSLContext sslContext = SSLContext.getInstance("TLS");
//initialize the ssl-context
sslContext.init(kmf.getKeyManagers(),null,null);
// We're ready for the engine.
SSLEngine engine = sslContext.createSSLEngine(host, port);
// Use as client
engine.setUseClientMode(true);
