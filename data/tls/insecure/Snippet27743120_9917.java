private static void addSSLSupport(DefaultIoFilterChainBuilder chain)
        throws Exception {
    try {
        KeyStore keyStore=KeyStore.getInstance("JKS");
        char[] passphrase= {'t','e','s','t','s','s','l'};
        keyStore.load(new FileInputStream("/root/mydomain.jks"),passphrase);
        Util.logInfo("Key Store loaded");
        KeyManagerFactory kmf = KeyManagerFactory
             .getInstance(KEY_MANAGER_FACTORY_ALGORITHM);
        kmf.init(keyStore, passphrase);
        SSLContext ctx=SSLContext.getInstance("TLS");
        ctx.init(kmf.getKeyManagers(), null, null);            
        SslFilter sslFilter = new SslFilter(ctx);
        chain.addLast("sslFilter", sslFilter);
        Util.logInfo("SSL ON");
    }catch(Exception e){
        Util.logError(e.toString());
        throw e;
    }
}
