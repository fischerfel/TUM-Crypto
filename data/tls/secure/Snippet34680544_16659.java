SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
ServerSocketFactory sf = SSLServerSocketFactory.getDefault();
KeyManager[] km = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).getKeyManagers();
TrustManager[] tm = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).getTrustManagers();
SecureRandom random = new SecureRandom();
sslContext.init(km, tm, random);
requestContext.put(BindingProviderProperties.SSL_SOCKET_FACTORY, sslContext.getSocketFactory());
