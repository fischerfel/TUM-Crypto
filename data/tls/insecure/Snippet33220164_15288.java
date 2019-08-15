public static SSLServerSocket getServerSocketWithCert(int port, InputStream pathToCert, String passwordFromCert) throws IOException,
                                KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException, UnrecoverableKeyException{
    TrustManager[] tmm;
    KeyManager[] kmm;
    KeyStore ks  = KeyStore.getInstance("JKS");
    ks.load(pathToCert, passwordFromCert.toCharArray());
    tmm=tm(ks);
    kmm=km(ks, passwordFromCert);
    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(kmm, tmm, null);
    SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) ctx.getServerSocketFactory();
    SSLServerSocket ssocket = (SSLServerSocket)        socketFactory.createServerSocket(port);
    return ssocket;
}
private static TrustManager[] tm(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
    TrustManagerFactory trustMgrFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustMgrFactory.init(keystore);
    return trustMgrFactory.getTrustManagers();
};
private static KeyManager[] km(KeyStore keystore, String password) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
    KeyManagerFactory keyMgrFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyMgrFactory.init(keystore, password.toCharArray());
    return keyMgrFactory.getKeyManagers();
};

    public static void main(String[] args){
        SSLServerSocket ss = null;
        try {
            ss = getServerSocketWithCert(12345, Server.class.getResourceAsStream("/privateKey.store"), "password");
        } catch(BindException e){
            e.printStackTrace();
            System.exit(1);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        while(true){
            SSLSocket s = ss.accept();
            new DataOutputStream(s.getOutputStream()).writeUTF("test");
            //TODO ERROR IS APPENING HERE
        }
    }
