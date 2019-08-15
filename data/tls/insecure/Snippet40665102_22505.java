private boolean SslTlsConnection() throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {

        KeyStore client = KeyStore.getInstance("JKS");
        client.load(new FileInputStream(currentPath + "clientcert.keystore"), Password.toCharArray());

        KeyStore trust = KeyStore.getInstance("JKS");
        trust.load(new FileInputStream(currentPath + "myTrustStore.keystore"), Password.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(client, Password.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(trust);

        SSLContext sc = SSLContext.getInstance("SSL");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        sc.init(kmf.getKeyManagers(), trustManagers, new java.security.SecureRandom());

        SSLSocketFactory ssf = sc.getSocketFactory();
        sslsocket = (SSLSocket) ssf.createSocket(Properties.host, Properties.portNumber);

        sslsocket.startHandshake();
        System.out.println("Handshaking Complete");
        System.out.println("Just connected to " + sslsocket.getInetAddress() + "\n");

        transport = new IOTransport(sslsocket);
        return false;
}


private void close(){
 sslsocket.close();
}
