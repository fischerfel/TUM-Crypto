private static final String CA_CERTS_PATH = System.getProperties().getProperty("java.home") + File.separator +
        "lib" + File.separator + "security" + File.separator + "cacerts";

private HttpClient createHttpClient(HttpDestination httpDestination)
        throws DestinationException, KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, KeyManagementException {

    HttpClient httpClient = httpDestination.createHttpClient();
    try (FileInputStream fis = new FileInputStream(CA_CERTS_PATH)) {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(fis, "changeit".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, tmf.getTrustManagers(), new SecureRandom());

        // Instantiate the custom SSLSocketFactory
        SSLSocketFactory sslSocketFactory = new CustomSSLSocketFactory(
                ctx,
                SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER,
                new String[]{"TLSv1.1", "TLSv1.2"}
        );

        // Register the https scheme with the custom SSLSocketFactory
        Scheme httpsScheme = new Scheme("https", 443, sslSocketFactory);
        httpClient.getConnectionManager().getSchemeRegistry().register(httpsScheme);
    }
    return httpClient;
}
