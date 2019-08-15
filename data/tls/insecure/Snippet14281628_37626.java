public static SSLSocketFactory getMyCustomClientCertSocketFactory(String pemPath,
        boolean verifyPeer)
        throws NoSuchAlgorithmException, FileNotFoundException, IOException,
        KeyStoreException, CertificateException, UnrecoverableKeyException,
        KeyManagementException, InvalidKeySpecException {
    SSLContext context = SSLContext.getInstance("TLS");

    byte[] certAndKey = IOUtil.fileToBytes(new File(pemPath));
    byte[] certBytes = parseDERFromPEM(certAndKey,
            "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
    byte[] keyBytes = parseDERFromPEM(certAndKey,
            "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");

    X509Certificate cert = generateX509CertificateFromDER(certBytes);
    RSAPrivateKey key = generateRSAPrivateKeyFromDER(keyBytes);

    KeyStore keystore = KeyStore.getInstance("JKS");
    keystore.load(null);
    keystore.setCertificateEntry("cert-alias", cert);
    keystore.setKeyEntry("key-alias", key, "changeit".toCharArray(),
            new Certificate[]{cert});

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(keystore, "changeit".toCharArray());

    KeyManager[] km = kmf.getKeyManagers();

    TrustManager[] tm = null;

    if (!verifyPeer) {
        tm = new TrustManager[]{new TrustyTrustManager()};
    }

    context.init(km, tm, null);

    return context.getSocketFactory();
}
