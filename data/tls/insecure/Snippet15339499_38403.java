public EwsSSLProtocolSocketFactory() {
    super();
}

private static SSLContext createEasySSLContext() {
    try {
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(
            null, 
            new TrustManager[] {new EwsX509TrustManager(null, trustManager)}, 
            null);
        return context;
    } catch (Exception e) {
        System.out.println(e.getMessage()+e);
        throw new HttpClientError(e.toString());
    }
}

private SSLContext getSSLContext() {
    if (this.sslcontext == null) {
        this.sslcontext = createEasySSLContext();
    }
    return this.sslcontext;
}
