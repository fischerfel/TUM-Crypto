    public class SSLInfo {
    public void sslContextInitialization() throws NoSuchAlgorithmException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        try {
            ctx.init(new KeyManager[0],
                    new TrustManager[] { new DefaultTrustManager() },
                    new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
//      SSLContext.setDefault(ctx);
        HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
    }
}


class DefaultTrustManager implements X509TrustManager {
    public DefaultTrustManager() {
    }

    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
    }

    public 509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
