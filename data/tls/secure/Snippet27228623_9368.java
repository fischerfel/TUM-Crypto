private javax.net.ssl.SSLContext sslContext;

private org.apache.http.client.CredentialsProvider credentialsProvider;

public void createSSLContext(String pin) {
    InputStream store = null;
    try {
        String password = getPassword();
        store = getResources().openRawResource(R.raw.key_store);

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(store, password.toCharArray());

        KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        keyFactory.init(keyStore, password.toCharArray());
        trustFactory.init(keyStore);

        String cn = null;
        List<String> aliases = Collections.list(keyStore.aliases());
        for (String alias : aliases) {
            if (!alias.equalsIgnoreCase("server")) {
                cn = alias;
                break;
            }
        }

        sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(keyFactory.getKeyManagers(), trustFactory.getTrustManagers(), null);

        credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(String.format("%s:%s", cn, pin)));
    } catch (Throwable t) {
        Log.e(Program.TAG, t.getMessage(), t);
        throw new RuntimeException(t.getMessage(), t);
    } finally {
        if (store != null) {
            try {
                store.close();
            } catch (IOException e) {
                Log.e(Program.TAG, e.getMessage(), e);
            }
        }
    }
}

public SSLContext getSSLContext() {
    return sslContext;
}

public HttpContext getHttpContext() {
    HttpClientContext httpContext = HttpClientContext.create();
    httpContext.setCredentialsProvider(credentialsProvider);

    return httpContext;
}
