public class MyClientSocketFactory implements RMIClientSocketFactory, Serializable {

    public MyClientSocketFactory() {}

    public Socket createSocket(String host, int port) {

        SecureRandom sr = new SecureRandom();
        sr.nextInt();

        KeyStore serverKeyStore = KeyStore.getInstance("JKS");
        FileInputStream server = new FileInputStream("src/server.public");
        String passphrase = //
        serverKeyStore.load(server, passphrase.toCharArray());
        server.close();

        KeyStore clientKeyStore = KeyStore.getInstance("JKS");
        FileInputStream client = new FileInputStream("src/client.private");
        String password = //
        clientKeyStore.load(client, password.toCharArray());
        client.close();

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(serverKeyStore);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(clientKeyStore, password.toCharArray());

        SSLContext SSLC = SSLContext.getInstance("TLS");
        SSLC.init(kmf.getKeyManagers(), tmf.getTrustManagers(), sr);

        SSLSocketFactory sf = SSLC.getSocketFactory();
        SSLSocket socket = (SSLSocket) sf.createSocket(host, port);

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
