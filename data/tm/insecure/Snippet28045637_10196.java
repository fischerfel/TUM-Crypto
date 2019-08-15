public class TrustingX509TrustManager implements X509TrustManager
{
    /**
     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // return null will let jsse accept all certificates!
        return null;
    }

    /**
     * @see javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[],String authType)
     */
    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }

    /**
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[],String authType)
     */
    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }
}
