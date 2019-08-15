            SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(null, new TrustManager[]{tm}, null);
    SSLSocketFactory ssf = new SSLSocketFactory(ctx);
    ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    ClientConnectionManager ccm = client.getConnectionManager();
    SchemeRegistry sr = ccm.getSchemeRegistry();
    sr.register(new Scheme("https", ssf, 443));
    return new DefaultHttpClient(ccm, client.getParams());
