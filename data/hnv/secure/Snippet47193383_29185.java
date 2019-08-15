public HttpsURLConnection getSecureConnection(final URL url, final String method, final int connectTimeout,
                                              final int readTimeout) throws IOException {
    Validate.notNull(sslContext);
    Validate.notNull(url);
    Validate.notNull(method);
    Validate.isTrue(connectTimeout > 0);
    Validate.isTrue(readTimeout > 0);
    HttpsURLConnection connection;
    try {
        connection = (HttpsURLConnection) url.openConnection();
    } catch (final IOException ioe) {
        LOGGER.error("[CertificateLoader] Unable to open URL connection!", ioe);
        throw new IOException("Unable to open URL connection!", ioe);
    }
    connection.setSSLSocketFactory(sslContext.getSocketFactory());
    connection.setRequestMethod(method);
    connection.setConnectTimeout(connectTimeout);
    connection.setReadTimeout(readTimeout);
    connection.setHostnameVerifier(NoopHostnameVerifier.INSTANCE);
    if (method.equals("PUT")) {
        connection.setRequestProperty("Content-Length", "0");
    }
    if (connection.getContentLength() > 0) {
        Object foo = connection.getContent();
        LOGGER.error("This is what's in here: " + foo.toString());
    }
    return connection;
}
