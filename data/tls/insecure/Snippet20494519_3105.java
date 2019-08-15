@Override
protected final ClientConnectionManager createClientConnectionManager() {
    final SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), MAFSecurityConstants.HTTP_PORT));
    try {
        registry.register(new Scheme("https", newSslSocketFactory(), MAFSecurityConstants.HTTPS_PORT));
    } catch (Exception e) {
        Log.e("NEW_SSL_SOCKET_FACTORY", "Error:" + e.getMessage());
    }
    return new ThreadSafeClientConnManager(getParams(), registry);
}

private class MYSSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    @SuppressLint("NewApi")
    public MYSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, UnrecoverableKeyException, KeyChainException, InterruptedException,
            CertificateException, IOException {
        super(truststore);
        String alias = "sslkeystore";
        X509Certificate[] chain = KeyChain.getCertificateChain(mContext, alias);
        if (chain != null) {
            for (X509Certificate crt : chain) {
                Log.v("NEWS", "Certificate Type: " + crt.getType());
                Log.v("NEWS", "Certificate Name: " + crt.getSubjectDN().getName());
            }
        } else {
            Log.v("NEWS", "No Certificates.");
        }
        PrivateKey privateKey = KeyChain.getPrivateKey(mContext, alias);

        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(null);
        keyStore.setKeyEntry(alias, privateKey.getEncoded(), chain);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(truststore);
        sslContext.init(null, tmf.getTrustManagers(), null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
            UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}

@SuppressLint("NewApi")
private SSLSocketFactory newSslSocketFactory(){
    try {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        SSLSocketFactory sf = new MYSSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return sf;
    } catch (NoSuchAlgorithmException e) {
        Log.v("NEWS", "Error: " + e.getMessage());
        e.printStackTrace();
    } catch (KeyManagementException e) {
        Log.v("NEWS", "Error: " + e.getMessage());
        e.printStackTrace();
    } catch (KeyStoreException e) {
        Log.v("NEWS", "Error: " + e.getMessage());
        e.printStackTrace();
    } catch (CertificateException e) {
        Log.v("NEWS", "Error: " + e.getMessage());
        e.printStackTrace();
    } catch (IOException e) {
        Log.v("NEWS", "Error: " + e.getMessage());
        e.printStackTrace();
    } catch (UnrecoverableKeyException e) {
        Log.v("NEWS", "Error: " + e.getMessage());
        e.printStackTrace();
    } catch (KeyChainException e) {
        Log.v("NEWS", "Error: " + e.getMessage());
        e.printStackTrace();
    } catch (InterruptedException e) {
        Log.v("NEWS", "Error: " + e.getMessage());
        e.printStackTrace();
    }
}
