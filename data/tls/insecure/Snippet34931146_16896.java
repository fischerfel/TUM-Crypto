FileInputStream keyStoreInputStream = new FileInputStream(KEYSTORE_PATH);

KeyStore keyStore = KeyStore.getInstance("JKS");
keyStore.load(null, null);
keyStore.load(keyStoreInputStream, KEY_PASSWORD);

KeyManagerFactory keyManagerFactory =
    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyManagerFactory.init(keyStore, KEY_PASSWORD);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
SSLServerSocket sslServerSocket =
    (SSLServerSocket) sslServerSocketFactory.createServerSocket(4444);

while (true) {
    SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();

    InputStream inputStream = sslSocket.getInputStream();

    FileOutputStream outputStream = new FileOutputStream(OUTPUT_PATH,false);

    byte[] bytes = new byte[12 * 1024];
    int count;
    while ((count = inputStream.read(bytes)) > 0) {
        outputStream.write(bytes, 0, count);
    }
}
