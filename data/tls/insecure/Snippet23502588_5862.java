public class MyServerSocketFactory implements RMIClientSocketFactory, Serializable {

    public MyServerSocketFactory() {}

    public Socket createSocket(String host, int port) {

        SecureRandom sr = new SecureRandom();
        sr.nextInt();

        KeyStore clientKeyStore = KeyStore.getInstance("JKS");
        FileInputStream client = new FileInputStream("src/client.public");
        String passphrase = //
        clientKeyStore.load(client, passphrase.toCharArray());
        client.close();

        KeyStore serverKeyStore = KeyStore.getInstance("JKS");
        FileInputStream server = new FileInputStream("src/server.private");
        String password = //
        serverKeyStore.load(server, password.toCharArray());
        server.close();

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(serverKeyStore);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(clientKeyStore, password.toCharArray());

        SSLContext SSLC = SSLContext.getInstance("TLS");
        SSLC.init(kmf.getKeyManagers(), tmf.getTrustManagers(), sr);

        SSLServerSocketFactory sf = SSLC.getServerSocketFactory();
        SSLServerSocket socket = (SSLServerSocket) sf.createServerSocket(host, port);

        return socket;
    }

    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }

}
