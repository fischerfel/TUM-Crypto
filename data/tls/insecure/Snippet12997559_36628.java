static SSLSocketFactory getSocketFactory (final String caCrtFile, final String crtFile, final String keyFile, final String password) throws Exception
{ 
    Security.addProvider(new BouncyCastleProvider());

    PEMReader reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(caCrtFile)))));
    X509Certificate caCert = (X509Certificate)reader.readObject();
    reader.close();

    reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(crtFile)))));
    X509Certificate cert = (X509Certificate)reader.readObject();
    reader.close();

    reader = new PEMReader(
            new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(keyFile)))),
            new PasswordFinder() {
                public char[] getPassword() {
                    return password.toCharArray();
                }
            }
    );
    KeyPair key = (KeyPair)reader.readObject();
    reader.close();

    KeyStore caKs = KeyStore.getInstance("JKS");
    caKs.load(null, null);
    caKs.setCertificateEntry("ca-certificate", caCert);
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
    tmf.init(caKs);

    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(null, null);
    ks.setCertificateEntry("certificate", cert);
    ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(), new java.security.cert.Certificate[]{cert});
    //ks.setKeyEntry("public-key", key.getPublic(), password.toCharArray(), new java.security.cert.Certificate[]{cert});
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, password.toCharArray());

    SSLContext context = SSLContext.getInstance("SSLv3");
    context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    return context.getSocketFactory();
}
