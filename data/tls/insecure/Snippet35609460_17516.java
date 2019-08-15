public class Server {
    public static final int PORT = 4646;
    TrustManagerFactory tmf;
    KeyManagerFactory kmf;
    KeyStore publicKeyStore;
    KeyStore privateKeyStore;
    SSLServerSocket serverSocket;

    public static void main(String args[]) {
        Server server = new Server();
        server.init();
    }

    private void init() {
        InputStream privateKeyStoreIns;
        InputStream publicKeyStoreIns;

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextInt();

        privateKeyStoreIns = Server.class.getResourceAsStream("/server.private");
        publicKeyStoreIns = Server.class.getResourceAsStream("/client.public");

        Security.addProvider(new BouncyCastleProvider());

        try {
            privateKeyStore = setupKeystore(privateKeyStoreIns, "private");
            publicKeyStore = setupKeystore(publicKeyStoreIns, "public");

            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(publicKeyStore);

            kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(privateKeyStore, "private".toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(),
                    tmf.getTrustManagers(),
                    secureRandom);

            SSLServerSocketFactory sf = sslContext.getServerSocketFactory();
            serverSocket = (SSLServerSocket) sf.createServerSocket( PORT );
            serverSocket.setNeedClientAuth(true);

            Socket socket = serverSocket.accept();

            ObjectInputStream objInputStream = new ObjectInputStream(socket.getInputStream());
            while (objInputStream.readObject() != null) {
                // do nothing
            }

            objInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private KeyStore setupKeystore(InputStream keyStoreInputStream, String passphrase)
            throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(keyStoreInputStream, passphrase.toCharArray());

        return keyStore;
    }

}
