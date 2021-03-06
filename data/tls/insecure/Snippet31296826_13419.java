server = HttpsServer.create(new InetSocketAddress( SERVER_PORT), conversationCount);
SSLContext sslContext = SSLContext.getInstance("TLS");
char[] password= "XXXXX".toCharArray();
KeyStore ks = KeyStore.getInstance("JKS");
FileInputStream fis = new FileInputStream("server_keystore");
ks.load(fis, password);
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(ks, password);
TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
tmf.init(ks);
sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
