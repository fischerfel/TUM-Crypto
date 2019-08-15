static String CA_FILE = "ca-rsa-cert.pem";

public static void main(String[] args) throws Exception
{
    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(new FileInputStream(CA_FILE), null);

    TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ks);

    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, tmf.getTrustManagers(), null);

    // Redirected through hosts file
    URL url = new URL("https://example.com:8443");

    HttpsURLConnection connection = (HttpsURLConnection) url
            .openConnection();
    connection.setSSLSocketFactory(context.getSocketFactory());

    ...
}
