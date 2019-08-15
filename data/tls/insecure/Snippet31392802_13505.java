public class TestProxy {
public static void main(String[] args) {
    Series<Parameter> parameters = null;
    ClientResource clientResource = null;
    Representation representation = null;

    try {

        Client client = new Client(new Context(), Protocol.HTTPS);
        parameters = client.getContext().getParameters();
        // proxy with credentials Works as excepted with http sites
        parameters.add("proxyHost", PROXYIP);
        parameters.add("proxyPort", PROXYPORT);
        // create trust manager that trusts everything to eliminate
        // certificates as an issue
        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkServerTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
                // This will never throw an exception.
                // This doesn't check anything at all: it's insecure.
            }
        };
        final SSLContext sslContext = SSLContext.getInstance("TLS");

        sslContext.init(null, new TrustManager[] { tm }, null);
        Context context = client.getContext();
        context.getAttributes().put("sslContextFactory",
                new SslContextFactory() {
                    public void init(Series<Parameter> parameters) {
                    }

                    public SSLContext createSslContext() {
                        return sslContext;
                    }
                });
        clientResource = new ClientResource("https://www.google.com");
        clientResource.setProxyChallengeResponse(
                ChallengeScheme.HTTP_BASIC, USER, PASS);
        clientResource.setNext(client);
        representation = clientResource.get();
        System.out.println(representation.getText());
        clientResource.get();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
