String keyStorePath = "keystore.jks";
String keyStorePassword = "password";

KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
KeyStore keyStore = new KeyStore();
keyStore.load(new FileInputStream(keyStorePath), keyStorePassword);
keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

sslContext = SSLContext.getInstance("TLS");
sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

SSLContext sslContext = getServerSSLContext(namespace.getUuid());
SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();

// Create sockets as necessary
