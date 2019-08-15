static {
    try {
        SSL_CONTEXT = SSLContext.getInstance("SSL");
        SSL_CONTEXT.init(null, new TrustManager[] { new LocalSSLTrustManager() }, null);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Unable to initialise SSL context", e);
    } catch (KeyManagementException e) {
        throw new RuntimeException("Unable to initialise SSL context", e);
    }
}

public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
    LOG.trace("createSocket(host => {}, port => {})", new Object[] { host, new Integer(port) });

    return SSL_CONTEXT.getSocketFactory().createSocket(host, port);
}
