private void initialiseConnection() {

    XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
            .builder();
    config.setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible);
    config.setServiceName(serverAddress); //mydomain.com
    config.setHost(serverAddress);
    config.setPort(5222);
    config.setDebuggerEnabled(true);

    SSLContext sslContext = null;

    try {
        sslContext = createSSLContext(context);
    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    }

    config.setCustomSSLContext(sslContext);
    config.setSocketFactory(sslContext.getSocketFactory());

    XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
    XMPPTCPConnection.setUseStreamManagementDefault(true);

    connection = new XMPPTCPConnection(config.build());
    XMPPConnectionListener connectionListener = new XMPPConnectionListener();
    connection.addConnectionListener(connectionListener);
}

private SSLContext createSSLContext(Context context) throws KeyStoreException,
        NoSuchAlgorithmException, KeyManagementException, IOException, CertificateException {

    KeyStore trustStore;
    InputStream in = null;
    trustStore = KeyStore.getInstance("BKS");

    in = context.getResources().openRawResource(R.raw.my_keystore);

    trustStore.load(in, "MyPassword123".toCharArray());

    TrustManagerFactory trustManagerFactory = TrustManagerFactory
            .getInstance(KeyManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(trustStore);
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
    return sslContext;
}
