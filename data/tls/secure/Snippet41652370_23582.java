SSLContext sslContext = null;
try {
    sslContext = SSLContext.getInstance("TLSv1.2");
    SSLContext.setDefault(sslContext );
} catch (NoSuchAlgorithmException e) {
    log.error("Failure getting ssl context", e);
}
