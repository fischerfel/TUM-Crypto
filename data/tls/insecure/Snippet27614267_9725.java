private static void addSSLSupport(DefaultIoFilterChainBuilder chain)
        throws Exception {
    try {
        KeyStore keyStore=KeyStore.getInstance("JKS");
        char[] passphrase= {'t','e','s','t','s','s','l'};
        keyStore.load(new FileInputStream("/home/ec2-user/digicert/mydomain.jks"),passphrase);
        Util.logInfo("Key Store loaded");
        SSLContext ctx=SSLContext.getInstance("TLS");
        TrustManagerFactory trustFactory=TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustFactory.init(keyStore);
        X509TrustManager defaultTrustManager = (X509TrustManager) trustFactory.getTrustManagers()[0];
        ctx.init(null, trustFactory.getTrustManagers(), null);            
        SslFilter sslFilter = new SslFilter(ctx);
        chain.addLast("sslFilter", sslFilter);
        Util.logInfo("SSL ON");
    }catch(Exception e){
        Util.logError(e.toString());
        throw e;
    }
}
