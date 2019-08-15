private void openSSLSocketWithCA(String mHost, int mPort) {
    try{

        // Load CAs from an InputStream
        // (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // From https://www.washington.edu/itconnect/security/ca/load-der.crt

        InputStream caInput = am.open("star_******_chained.crt");

        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        SSLSocketFactory sf = context.getSocketFactory();
        mSocket = (SSLSocket) sf.createSocket(mHost, mPort);
        mSocket.setEnabledProtocols(new String[]{"TLSv1"});
        mInputStream = mSocket.getInputStream();
        mOutputStream = mSocket.getOutputStream();

        mSocket.setSoTimeout(0);
    }catch (Exception e){
        e.printStackTrace();
    }
}
