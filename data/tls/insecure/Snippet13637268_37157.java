public void run() {
    try {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(service.getBaseContext().getResources().openRawResource(R.raw.keystore),
                "password".toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, "password".toCharArray());

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        ServerSocketFactory socketFactory = sslContext.getServerSocketFactory();
        SSLServerSocket mServerSocket = (SSLServerSocket) socketFactory.createServerSocket(8080);
        while (!mServerSocket.isClosed()) {

            System.out.println("waiting");
            SSLSocket client = (SSLSocket) mServerSocket.accept();

            client.addHandshakeCompletedListener(new HandshakeCompletedListener() {

                public void handshakeCompleted(HandshakeCompletedEvent arg0) {
                    System.out.println("handshakeCompleted");

                }

            });
            client.startHandshake(); // MultiThreadWebServer.java:136

            client.getOutputStream().flush();

            client.close();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
