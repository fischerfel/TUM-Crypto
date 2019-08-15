KeyStore ks = KeyStore.getInstance("PKCS12");
ks.load(new FileInputStream("/pathToFile/cert.pfx"), "password".toCharArray());
KeyManagerFactory kmf = KeyManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
kmf.init(ks, "password".toCharArray());
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(kmf.getKeyManagers(), null, null);

HttpsTransportSE httpTransport = new HttpsTransportSE("10.10.8.221", 1246, "/WebService?singleWsdl", timeOut);
((HttpsServiceConnectionSE) httpTransport.getServiceConnection()).setSSLSocketFactory(sslContext.getSocketFactory());
