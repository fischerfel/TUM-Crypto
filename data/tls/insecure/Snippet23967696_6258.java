public void startTLS() {

    try {
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, null);
    } catch(NoSuchAlgorithmException nsa) {
        System.out.println("Exception : No Such Algorithm");
    } catch(KeyManagementException kme) {
        System.out.println("Exception : KeyManagementException:");
    }//try-catch


    IoFilterChain chain = session.getFilterChain();
    SslFilter sslFilter = (SslFilter) chain.get("sslFilter");

    if (sslFilter == null) {
        sslFilter = new SslFilter(sslContext);
        sslFilter.setUseClientMode(true);
        if ((cipherSuites != null) && !cipherSuites.isEmpty()) {
            sslFilter.setEnabledCipherSuites(cipherSuites.toArray( new String[cipherSuites.size()] )); 
        } 

        chain.addFirst("sslFilter", sslFilter);

    }else {
        try {
            sslFilter.startSsl(this.session);
        } catch(SSLException se) {
            System.out.println("SslException:"+se);
        }
    }//if-else

}//startTLS
