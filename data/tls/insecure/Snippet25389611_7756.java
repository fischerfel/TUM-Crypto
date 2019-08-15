public void testKeys() throws Exception {
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

    ks.load(getContext().getResources().openRawResource(R.raw.keystore), "storepass".toCharArray());
    kmf.init(ks, "storepass".toCharArray());


    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
    ts.load(getContext().getResources().openRawResource(R.raw.keystore), "storepass".toCharArray());
    tmf.init(ts);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    AsyncHttpServer httpServer = new AsyncHttpServer();
    httpServer.listenSecure(8888, sslContext);
    httpServer.get("/", new HttpServerRequestCallback() {
        @Override
        public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
            response.send("hello");
        }
    });

    Thread.sleep(1000);

    AsyncHttpClient.getDefaultInstance().getSSLSocketMiddleware().setSSLContext(sslContext);
    AsyncHttpClient.getDefaultInstance().getSSLSocketMiddleware().setTrustManagers(tmf.getTrustManagers());
    AsyncHttpClient.getDefaultInstance().executeString(new AsyncHttpGet("https://localhost:8888/"), null).get();
}
