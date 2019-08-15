SSLContext sc = null;
SslFilter sslFilter;
private void startTLS() {
    try {
        sc = SSLContext.getInstance("TLSv1");
        sc.init(null, null, null);
        sslFilter = new SslFilter(sc);
        sslFilter.setUseClientMode(true);
        session.getFilterChain().addFirst("mySSL", sslFilter);
    } catch(Exception e) {
        e.printStackTrace();
    }    
}
