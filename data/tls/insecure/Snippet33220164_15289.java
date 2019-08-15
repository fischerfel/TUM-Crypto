public static SSLSocket getSocketWithCert(InetAddress ip, int port, InputStream pathToCert, String passwordFromCert) throws IOException,
                                KeyManagementException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
    TrustManager[] tmm;
    KeyStore ks  = KeyStore.getInstance("BKS");
    ks.load(pathToCert, passwordFromCert.toCharArray());
    tmm=tm(ks);
    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(null, tmm, null);
    SSLSocketFactory SocketFactory = (SSLSocketFactory) ctx.getSocketFactory();
    SSLSocket socket = (SSLSocket) SocketFactory.createSocket();
    socket.connect(new InetSocketAddress(ip, port), 5000);
    return socket;
}

private static TrustManager[] tm(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
    TrustManagerFactory trustMgrFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustMgrFactory.init(keystore);
    return trustMgrFactory.getTrustManagers();
};
public static void(String[] args){
        int id;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            id = R.raw.publickey;
        } else {
            id = R.raw.publickey_v1;
        }
        try {
            Socket s = SSLSocketKeystoreFactory.getSocketWithCert("myip", 12345, HackerMainActivity.this.getResources().openRawResource(id), "password");
        } catch (UnknownHostException | SecurityException e) {
            e.printStackTrace();
            return;
        } catch(SocketTimeoutException e){
            e.printStackTrace();
            return;
        } catch (KeyManagementException | NoSuchAlgorithmException | CertificateException | KeyStoreException e) {
            e.printStackTrace();
        }
        DataInputStream in = new DataInputStream(s.getInputStream());
        //TODO ERROR IS APPENING HERE
}
