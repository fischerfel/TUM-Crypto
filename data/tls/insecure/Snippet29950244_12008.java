// create SSL Context which trusts your self-signed certificate
TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
keystore.load(ClassLoader.getSystemResourceAsStream("myKeystoreFile"), "password".toCharArray());
trustManagerFactory.init(keystore);
TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(null, trustManagers, null);

// register your trusting SSL context
Protocol.registerProtocol("https",
        new Protocol("https", (ProtocolSocketFactory) new SocketFactoryWrapper(sslContext.getSocketFactory()), 443));

// make the https call
HttpClient httpclient = new HttpClient();
GetMethod httpget = new GetMethod("https://myendpoint.com");
httpclient.executeMethod(httpget);
System.out.println(httpget.getStatusLine());
