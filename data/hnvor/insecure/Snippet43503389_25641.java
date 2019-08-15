public static String gerarRequestHttpsSemCertificado(final URL url, final JSONObject jsonObject) throws Exception {
    String retorno = "";
    BufferedReader reader = null;

    try {

        final HttpURLConnection httpConnection = (HttpsURLConnection) url.openConnection();

        UtilWS.setAcceptAllVerifier((HttpsURLConnection) httpConnection);

        if (jsonObject != null) {
            final OutputStreamWriter out;

            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setDoOutput(true);
            httpConnection.setReadTimeout(0);
            httpConnection.setConnectTimeout(0);

            out = new OutputStreamWriter(httpConnection.getOutputStream());
            out.write(jsonObject.toString());
            out.close();

        }

        reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()), 1);

        final char[] buf = new char[1024];
        final StringBuilder sb = new StringBuilder();
        int count = 0;
        while (-1 < (count = reader.read(buf))) {
            sb.append(buf, 0, count);
        }
        retorno = sb.toString();

        reader.close();

    } catch (final IOException ex) {
        LogCefUtil.error(ex.getCause());
        LogCefUtil.error(ex.getMessage());

        if (null != reader) {
            reader.close();
        }
    }

    return retorno;
}

/**
 * Overrides the SSL TrustManager and HostnameVerifier to allow all certs
 * and hostnames. WARNING: This should only be used for testing, or in a
 * "safe" (i.e. firewalled) environment.
 *
 * @throws NoSuchAlgorithmException
 * @throws KeyManagementException
 */
private static void setAcceptAllVerifier(final HttpsURLConnection connection) throws NoSuchAlgorithmException, KeyManagementException {

    // Create the socket factory.
    // Reusing the same socket factory allows sockets to be
    // reused, supporting persistent connections.
    if (null == UtilWS.sslSocketFactory) {
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, UtilWS.ALL_TRUSTING_TRUST_MANAGER, new java.security.SecureRandom());
        UtilWS.sslSocketFactory = sc.getSocketFactory();
    }

    connection.setSSLSocketFactory(UtilWS.sslSocketFactory);

    // Since we may be using a cert with a different name, we need to ignore
    // the hostname as well.
    connection.setHostnameVerifier(UtilWS.ALL_TRUSTING_HOSTNAME_VERIFIER);
}

/** Atributo ALL_TRUSTING_TRUST_MANAGER. */
private static final TrustManager[] ALL_TRUSTING_TRUST_MANAGER = new TrustManager[] { new X509TrustManager() {
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    @Override
    public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
    }
} };

/** Atributo ALL_TRUSTING_HOSTNAME_VERIFIER. */
private static final HostnameVerifier ALL_TRUSTING_HOSTNAME_VERIFIER = new HostnameVerifier() {
    @Override
    public boolean verify(final String hostname, final SSLSession session) {
        return true;
    }
};
