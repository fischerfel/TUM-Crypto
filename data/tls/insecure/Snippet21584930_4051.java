try {
    SSLContext sslContext = SSLContext.getInstance("TLSv1");               
    sslContext.init(null, null, new SecureRandom());       

    sslFilter = new SslFilter(sslContext);
    sslFilter.setUseClientMode(true);
    sslFilter.setNeedClientAuth(false);
    session.getFilterChain().addFirst("mySSL", sslFilter);       
    session.setAttribute(SslFilter.DISABLE_ENCRYPTION_ONCE, Boolean.TRUE);
    assert session.getAttribute(SslFilter.DISABLE_ENCRYPTION_ONCE) == null;
} catch (Exception e) {
   e.printStactTrace();
