public class MyCustomClientHttpRequestFactory  extends SimpleClientHttpRequestFactory {

private static final Logger LOGGER = LoggerFactory
    .getLogger(MyCustomClientHttpRequestFactory.class);

private final HostnameVerifier hostNameVerifier;
private final ServerInfo serverInfo;

public MyCustomClientHttpRequestFactory (final HostnameVerifier hostNameVerifier,
    final ServerInfo serverInfo) {
    this.hostNameVerifier = hostNameVerifier;
    this.serverInfo = serverInfo;
}

@Override
protected void prepareConnection(final HttpURLConnection connection, final String httpMethod)
    throws IOException {
    if (connection instanceof HttpsURLConnection) {
        ((HttpsURLConnection) connection).setHostnameVerifier(hostNameVerifier);
        ((HttpsURLConnection) connection).setSSLSocketFactory(initSSLContext()
            .getSocketFactory());
    }
    super.prepareConnection(connection, httpMethod);
}

private SSLContext initSSLContext() {
    try {
        System.setProperty("https.protocols", "TLSv1");

        // Set ssl trust manager. Verify against our server thumbprint
        final SSLContext ctx = SSLContext.getInstance("TLSv1");
        final SslThumbprintVerifier verifier = new SslThumbprintVerifier(serverInfo);
        final ThumbprintTrustManager thumbPrintTrustManager =
            new ThumbprintTrustManager(null, verifier);
        ctx.init(null, new TrustManager[] { thumbPrintTrustManager }, null);
        return ctx;
    } catch (final Exception ex) {
        LOGGER.error(
            "An exception was thrown while trying to initialize HTTP security manager.", ex);
        return null;
    }
}
