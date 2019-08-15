    KeyStore ks = KeyStore.getInstance("JKS");

    // get user password and file input stream
    char[] password = ("mykspassword")).toCharArray();
    ClassLoader cl = this.getClass().getClassLoader();
    InputStream stream = cl.getResourceAsStream("myjks.jks");
    ks.load(stream, password);
    stream.close();

    SSLContext sc = SSLContext.getInstance("TLS");
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

    kmf.init(ks, password);
    tmf.init(ks);

    sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(),null);

    return sc.getSocketFactory();
