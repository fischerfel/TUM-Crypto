    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, getTrustingManager(), new java.security.SecureRandom());

    SSLSocketFactory socketFactory;
    socketFactory = new SSLSocketFactory(sc);
    Scheme sch = new Scheme("https", socketFactory, 443);
    client.getConnectionManager().getSchemeRegistry().register(sch);
