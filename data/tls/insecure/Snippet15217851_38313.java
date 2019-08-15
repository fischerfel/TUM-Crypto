private final SSLContext createSSLContext()
            throws NoSuchAlgorithmException, KeyStoreException,
            CertificateException, IOException,
            UnrecoverableKeyException, KeyManagementException {


        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(Config.getString(Config.KEYSTORE_PATH)));
        } catch (Exception ex) {
            throw new IOException("not found keystore file: " Config.getString(Config.KEYSTORE_PATH), ex);
        }
        try{
            keyStore.load(fis, Config.getString(Config.KEYSTORE_PASSWORD).toCharArray());
        }finally {
            IOUtils.closeQuietly(fis);
        }
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream in = new FileInputStream(Config.getString(Config.HTTPS_SERVER_CERT));
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(null);
        try {
            X509Certificate cacert = (X509Certificate) cf.generateCertificate(in);
            trustStore.setCertificateEntry("alias", cacert);
        } finally {
            IOUtils.closeQuietly(in);
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(trustStore);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, Config.getString(Config.KEYSTORE_PASSWORD).toCharArray());

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
