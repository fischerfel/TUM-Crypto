private SSLSocket getSSLSocket() throws TrackerSSLConnectionException {
    try {
        // Load properties.
        String keystore = properties.getProperty("keystore");
        String passphrase = properties.getProperty("keystorePassphrase");
        String host = properties.getProperty("host");
        int hostPort = Integer.parseInt(properties.getProperty("host_port"));

        // Create keystore.
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(keystore), passphrase.toCharArray());

        // Get factory for the given keystore.
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(null, tmf.getTrustManagers(), null);
        SSLSocketFactory factory = ctx.getSocketFactory();

        return (SSLSocket) factory.createSocket(host, hostPort);
    } catch (Exception e) {
        throw new TrackerSSLConnectionException(e.getMessage(), e.getCause());
    }
}

private String publishFetcher() throws TrackerSSLConnectionException, IOException {
    // Get socket connection.
    SSLSocket socket = getSSLSocket();

    // Get input and output streams from socket.
    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter output = new PrintWriter(socket.getOutputStream());

    // Request connection token to controller.
    String connectionToken = getConnectionToken(input, output);

    // Publish fetcher.
    final int rmiPort = Integer.parseInt(properties.getProperty("rmi_port"));
    TwitterFetcher fetcher = new TwitterFetcher(connector);
    Fetcher stub = (Fetcher) UnicastRemoteObject.exportObject(fetcher, 0);
    Registry registry = LocateRegistry.createRegistry(rmiPort);
    registry.rebind(connectionToken, stub);

    // Send RMI port.
    output.write(String.valueOf(rmiPort) + "\n");
    output.flush();

    input.close();
    output.close();

    return connectionToken;
}
