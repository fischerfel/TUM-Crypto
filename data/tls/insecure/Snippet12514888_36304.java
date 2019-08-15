protected static SSLSocketFactory getSocketFactoryPEM(String pemPath) throws Exception {        
    Security.addProvider(new BouncyCastleProvider());

    SSLContext context = SSLContext.getInstance("TLS");

    byte[] certAndKey = fileToBytes(new File(pemPath));

    String delimiter = "-----END CERTIFICATE-----";
    String[] tokens = new String(certAndKey).split(delimiter);

    byte[] certBytes = tokens[0].concat(delimiter).getBytes();
    byte[] keyBytes = tokens[1].getBytes();

    PEMReader reader;

    reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(certBytes)));
    X509Certificate cert = (X509Certificate)reader.readObject();        

    reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(keyBytes)));
    PrivateKey key = (PrivateKey)reader.readObject();        

    KeyStore keystore = KeyStore.getInstance("JKS");
    keystore.load(null);
    keystore.setCertificateEntry("cert-alias", cert);
    keystore.setKeyEntry("key-alias", key, "changeit".toCharArray(), new Certificate[] {cert});

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(keystore, "changeit".toCharArray());

    KeyManager[] km = kmf.getKeyManagers(); 

    context.init(km, null, null);

    return context.getSocketFactory();
}
