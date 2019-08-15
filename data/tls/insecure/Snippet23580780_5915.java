SSLSocketFactory factory = null;
    try {
    SSLContext ctx;
    KeyManagerFactory kmf;
    KeyStore ks;
    char[] passphrase = "passphrase".toCharArray();

    ctx = SSLContext.getInstance("TLS");
    kmf = KeyManagerFactory.getInstance("SunX509");
    ks = KeyStore.getInstance("JKS");
