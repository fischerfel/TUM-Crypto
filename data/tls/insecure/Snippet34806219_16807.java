public static SSLContext loadCertificate(String password)
            throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException,
            UnrecoverableKeyException, KeyManagementException {

        final FileInputStream fis = new FileInputStream(new File(CERT_FILE_DIRECTORY, CERT_FILE_NAME));

        final KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(fis, password.toCharArray());

        final KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(keyStore, null);
        final KeyManager[] keyManagers = kmf.getKeyManagers();
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, new TrustManager[]{new MyTrustManager()}, null);
        return sslContext;
    }
