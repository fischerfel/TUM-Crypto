    SSLContext ctxt = SSLContext.getInstance("TLSv1");
    ctxt.init(new KeyManager[0],new TrustManager[]{new DefaultTrustManager()},new SecureRandom());
    SSLSocketFactory factory = new SSLSocketFactory(ctxt);
    Scheme https = new Scheme("https", 443, factory);
    f_connectionManager.getSchemeRegistry().register(https);
