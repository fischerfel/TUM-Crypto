public class TrustManagerManipulator implements X509TrustManager {


    private static TrustManager[] trustManagers;
    private static final X509Certificate[] acceptedIssuers = new X509Certificate[] {};

    public boolean isClientTrusted(X509Certificate[] chain) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
        return true;
    }

    public static void allowAllSSL()
    {

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        SSLContext context = null;
        if (trustManagers == null) {
            trustManagers = new TrustManager[] { new TrustManagerManipulator() };
        }
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(context
                .getSocketFactory());
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return acceptedIssuers;
    }
}
