try {
    LOG.warn("Configuring SSL connection on port 8085");
    KeyStore keystore = KeyStore.getInstance("JCEKS");
    keystore.load(new FileInputStream("/.peg.jceks"),
            "password".toCharArray());

    KeyStore kstrust = KeyStore.getInstance("JCEKS");
    String truststorelocation = "/.peg.jceks";
    kstrust.load(new FileInputStream(truststorelocation), "changeit".toCharArray());

    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(keystore, "password".toCharArray());

    TrustManagerFactory tmf =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(kstrust);

    context = SSLContext.getInstance("TLS");
    context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    SSLServerSocketFactory sf = context.getServerSocketFactory();
    SSLServerSocket ss = (SSLServerSocket)sf.createServerSocket(8085);
    ss.setNeedClientAuth(true);

} catch (Exception e) {
    e.printStackTrace();
    LOG.error("Problem configuring SSL", e.getMessage());
}
