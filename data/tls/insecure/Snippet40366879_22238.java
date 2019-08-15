public SSLSocket createSSLSocket(String ipAddress, int port)
{
    try
    {
        SSLSocket socket = null;
        String certStorePassword = "password";
        String certStoreType = "pkcs12";

        InputStream iStream = getResources().openRawResource(R.raw.clientP12File);

        KeyStore keyStore = KeyStore.getInstance(certStoreType);
        keyStore.load(iStream, certStorePassword.toCharArray());
//            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, certStorePassword.toCharArray());

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(keyManagerFactory.getKeyManagers(), trustAllCerts, new SecureRandom());
        SSLContext.setDefault(sc);
        SSLSocketFactory factory = sc.getSocketFactory();
        socket = (SSLSocket) factory.createSocket(ipAddress, port);
        socket.setEnabledProtocols(new String[] { "TLSv1.2" });
        socket.setUseClientMode(true);
        socket.startHandshake();
        return socket;
    }
    catch (Exception ex) {
        Log.e(TAG, "createSSLSocket: ", ex);
    }

    return null;
}
