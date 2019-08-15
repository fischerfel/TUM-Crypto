KeyStore trustStore = KeyStore.getInstance("BKS");
InputStream trustStoreStream = context.getResources().openRawResource(R.raw.truststore);
trustStore.load(trustStoreStream, "<PASSWORD>".toCharArray());

TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(trustStore);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

Socket socket = sslContext.getSocketFactory().createSocket("ip", 1234);
... use socket
