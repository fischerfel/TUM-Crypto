private SSLServerSocketFactory handleCertificate() throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
    Security.addProvider(new BouncyCastleProvider());

    PEMReader pr = new PEMReader(new FileReader("p.pem"));
    X509CertificateObject cert = (X509CertificateObject) pr.readObject();

    PEMReader pr2 = new PEMReader(new FileReader("klient.cer"));
    X509CertificateObject cert2 = (X509CertificateObject) pr2.readObject();

    PEMReader kr = new PEMReader(new FileReader("001.key"),
            new PasswordFinder() {
                public char[] getPassword() {
                    return "password".toCharArray();
                }
            });

    KeyStore trustKeys = KeyStore.getInstance("JKS");
    trustKeys.load(null, "".toCharArray());
    trustKeys.setCertificateEntry("1", cert);

    KeyStore ksKeys = KeyStore.getInstance("JKS");
    ksKeys.load(null, "password".toCharArray());
    ksKeys.setCertificateEntry("1", cert2);

    org.bouncycastle.jce.provider.JCERSAPrivateCrtKey key;
    Object PK = kr.readObject();

    if (PK instanceof KeyPair) {
        key = (JCERSAPrivateCrtKey) ((KeyPair) PK).getPrivate();
    } else {
        key = (JCERSAPrivateCrtKey) PK;
    }

    ksKeys.setKeyEntry("1", key, "password".toCharArray(), new Certificate[] { cert2 });

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ksKeys, "password".toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(trustKeys);

    SSLContext sslContext = SSLContext.getInstance("SSLv3");
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());

    SSLServerSocketFactory factory = sslContext.getServerSocketFactory();
    return factory;
}
