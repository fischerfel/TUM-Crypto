class MySslRMIClientSocketFactory implements RMIClientSocketFactory {

    private SSLSocketFactory sf;

    public MySslRMIClientSocketFactory(SSLSocketFactory sf) {
        this.sf = sf;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return sf.createSocket(host, port);
    }   
}

---- inside the calling class

private static SSLContext getSSLContext(KeyStore keyStore) {
    // init KeyManagerFactory
    KeyManagerFactory keyManagerFactory = null;

    TrustManagerFactory trustManagerFactory = null;

    SSLContext sslContext = null;
    try {
        keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory
                .getDefaultAlgorithm());

        keyManagerFactory.init(keyStore, password.toCharArray());

        trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());

        trustManagerFactory.init(keyStore);

        // init KeyManager
        KeyManager keyManagers[] = keyManagerFactory.getKeyManagers();

        TrustManager trustManagers[] = trustManagerFactory
                .getTrustManagers();

        // init the SSL context

        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, trustManagers, new SecureRandom());

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnrecoverableKeyException e) {
        e.printStackTrace();
    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }

    return sslContext;
}

public static void main(String[] args) throws KeyStoreException,
        NoSuchAlgorithmException, CertificateException, IOException {
    KeyStore keystore = getAndLoadKeyStore();
    SSLContext sc = getSSLContext(keystore);

    SSLSocketFactory sf = sc.getSocketFactory();

    try {
        RMIClientSocketFactory csf = new MySslRMIClientSocketFactory(sf);
        Registry registry = LocateRegistry.getRegistry("myserver", 1234, csf);

        MyInterface obj = (MyInterface)registry.lookup("MyInterface");
        System.out.println("Found the registry entry");
        String message = obj.invokeMyMethod("arg1", 1);
        System.out.println(message);

    } catch (IOException | NotBoundException e) {
        e.printStackTrace();
    }
    System.out.println("Client done");
}
