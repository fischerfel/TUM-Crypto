public class Server implements ServerProtocol {
    public Server() {
        super();
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
        tmf.init(clientKeyStore);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(serverKeyStore, password.toCharArray());

        SSLContext SSLC = SSLContext.getInstance("TLS");
        SSLC.init(kmf.getKeyManagers(), tmf.getTrustManagers(), sr);

        SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
        SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory(null, null, true);

        LocateRegistry.createRegistry(2020).rebind("server", this);
        UnicastRemoteObject.exportObject(this, 2020, csf, ssf);
    }

    public void sayHello() {
        System.out.println("Hello");
    }
}
