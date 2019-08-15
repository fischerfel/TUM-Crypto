    private static TrustManager[] trustManagers;

public static class _FakeX509TrustManager implements
        javax.net.ssl.X509TrustManager {
    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
    }

    public boolean isClientTrusted(X509Certificate[] chain) {
        return (true);
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
        return (true);
    }

    public X509Certificate[] getAcceptedIssuers() {
        return (_AcceptedIssuers);
    }
}

public static void allowAllSSL() {

    javax.net.ssl.HttpsURLConnection
            .setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

    javax.net.ssl.SSLContext context = null;

    if (trustManagers == null) {
        trustManagers = new javax.net.ssl.TrustManager[] { new _FakeX509TrustManager() };
    }

    try {
        context = javax.net.ssl.SSLContext.getInstance("SSL");
        context.init(null, trustManagers, new SecureRandom());
    } catch (NoSuchAlgorithmException e) {
        Log.e("allowAllSSL", e.toString());
    } catch (KeyManagementException e) {
        Log.e("allowAllSSL", e.toString());
    }
    javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(context
            .getSocketFactory());
}
}
