KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
TrustManagerFactory trustManagerFactory=TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(keyStore);
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
socket = sslContext.getSocketFactory().createSocket(HOST, PORT);
socket.setKeepAlive(true);
