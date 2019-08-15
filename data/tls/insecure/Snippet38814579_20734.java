System.setProperty("javax.net.ssl.trustStore", "path to .jks");
System.setProperty("javax.net.ssl.trustStorePassword", "passowrd");
System.setProperty("javax.net.ssl.keyStore",  "path to .jks");
System.setProperty("javax.net.ssl.keyStorePassword", "password");
System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true"); 

final KeyStore keyStore = KeyStore.getInstance("JKS");  
keyStore.load(new FileInputStream("path to .jks"), "password".toCharArray());
final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyManagerFactory.init(keyStore, "password".toCharArray());

final KeyStore trustStore = KeyStore.getInstance("JKS");
trustStore.load(new FileInputStream("path to .jks"), "password".toCharArray());
final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(trustStore);

final SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
final SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket("abc.com", 5580);
sslSocket.startHandshake();
