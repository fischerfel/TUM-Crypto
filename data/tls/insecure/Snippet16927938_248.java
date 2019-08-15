public static void main(String[] args) throws Exception {
    Container container = createContainer();

    Server server = new ContainerServer(container);
    Connection connection = new SocketConnection(server);
    SocketAddress address = new InetSocketAddress(8443);

    KeyManagerFactory km = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    KeyStore serverKeystore = KeyStore.getInstance(KeyStore.getDefaultType());
    try(InputStream keystoreFile = new FileInputStream(SERVER_KEYSTORE_PATH)) {
        serverKeystore.load(keystoreFile, "asdfgh".toCharArray());
    }
    km.init(serverKeystore, SERVER_KS_PASSWORD.toCharArray());

    TrustManagerFactory tm = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    KeyStore caKeystore = KeyStore.getInstance(KeyStore.getDefaultType());
    try(InputStream caCertFile = new FileInputStream(CA_CERT_PATH)) {
        caKeystore.load(caCertFile, CA_KS_PASSWORD.toCharArray());
    }
    tm.init(caKeystore);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(km.getKeyManagers(), tm.getTrustManagers(), null);

    connection.connect(address, sslContext);
}
