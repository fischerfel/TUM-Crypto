SSLServerSocket server = null;
SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
char[] passwd = "testing".toCharArray();        //password for testing purposes

try {
    KeyStore ks = KeyStore.getInstance("JKS");
    FileInputStream store = new FileInputStream("<path to keystoke>");
    ks.load(store, passwd);
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, passwd);
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(ks);
    SSLContext sc = SSLContext.getInstance("TLS");
    sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    server = (SSLServerSocket) sslServerSocketFactory.createServerSocket(8189);
} catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException | UnrecoverableKeyException | KeyManagementException e) {
    e.printStackTrace();
    return;
}
while(!stopWork){
    SSLSocket incoming;
    Runnable r = null;
    try {
        incoming = (SSLSocket) server.accept();
        r = new ServerThread(incoming);
    } catch (IOException e) {
        e.printStackTrace();
    }
    Thread t = new Thread(r);
    t.start();
}
...
