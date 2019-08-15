SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(null, new TrustManager[]{new MyTrustManager()}, null);
Client client = ClientBuilder.newBuilder().sslContext(sslContext).build();
