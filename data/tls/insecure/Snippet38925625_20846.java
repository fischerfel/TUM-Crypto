private void initialiseConnection() {

    XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
            .builder();
    //config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
    config.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
    config.setServiceName(serverAddress);
    config.setHost(serverAddress);
    //config.setPort(5222);
    config.setPort(5222);
    config.setDebuggerEnabled(true);

    SSLContext sslContext = null;
    try {
        sslContext = createSSLContext(context);
        //sslContext.getSupportedSSLParameters();

    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (KeyManagementException e){
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();

    //} catch(NoSuchProviderException e){
    //    e.printStackTrace();
    }

    //config.setSocketFactory(new DummySSLSocketFactory());
    //config.setSocketFactory(SSLSocketFactory.getDefault());
    config.setCustomSSLContext(sslContext);
    //SSLSocketFactory socketFactory = sslContext.getSocketFactory();
    //NoSSLv3SocketFactory socketFactory = new NoSSLv3SocketFactory(sslContext.getSocketFactory());
    TLSSocketFactory socketFactory = new TLSSocketFactory(sslContext);
    //SSLSocketFactory noSSLv3Factory = new TlsOnlySocketFactory(sslContext.getSocketFactory());

    config.setSocketFactory(socketFactory);
    config.setEnabledSSLProtocols(new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"});


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

    in = context.getResources().openRawResource(R.raw.keystore1);

    trustStore.load(in, "MY_STORE_PASSWORD".toCharArray());

    TrustManagerFactory trustManagerFactory = TrustManagerFactory
            .getInstance(KeyManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(trustStore);
    //SSLContext sslContext = SSLContext.getInstance("TLS");
    SSLContext sslContext = null;
    try {
        //sslContext = SSLContext.getInstance("TLSv1", "AndroidOpenSSL");
        sslContext = SSLContext.getInstance("TLS");
    }
    catch(Exception e){
        e.printStackTrace();
    }

    sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());


    Log.d("SSL Protocol: ", sslContext.getProtocol());
    Log.d("SSL Provider: ", sslContext.getProvider().toString());
    String[] protocols = sslContext.getDefaultSSLParameters().getProtocols();
    sslContext.getDefaultSSLParameters().setProtocols(protocols);

    return sslContext;
}
