    public static void main(String[] args) throws Exception {

    ClientRequest req = new ClientRequest("https://127.0.0.16:8443/rest/lstgs/create", new ApacheHttpClient4Executor(doSSLBlackMagic()));

    // ... random stuff that has nothing to do with SSL

    ClientResponse<String> response = req.post(String.class);
    if (response.getStatus() != 201) {
        throw new RuntimeException("error, status: " + response.getStatus() + " / " + response.getEntity());
    } else {
        System.out.println(response.getEntity());
    }
}

public static KeyStore loadTrustStore() throws Exception {
    KeyStore trustStore = KeyStore.getInstance("jks");
    trustStore.load(new FileInputStream(new File("path-to-truststore")), "password".toCharArray());
    return trustStore;
}

protected static TrustManager[] getTrustManagers() throws Exception {
    KeyStore trustStore = loadTrustStore();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);
    return tmf.getTrustManagers();
}

public static HttpClient doSSLBlackMagic() throws Exception {
    SSLContext ctx = SSLContext.getInstance("TLS");
    TrustManager[] trustManagers = getTrustManagers();
    ctx.init(null, trustManagers, new SecureRandom());
    SSLSocketFactory factory = new SSLSocketFactory(ctx);

    HttpClient client = new DefaultHttpClient();
    ClientConnectionManager manager = client.getConnectionManager();
    manager.getSchemeRegistry().register(new Scheme("https", factory, 8443));

    return client;
}
