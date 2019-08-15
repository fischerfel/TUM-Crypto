SSLSocketFactory sf = new SSLSocketFactory(
    SSLContext.getInstance("TLS"),
    SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
