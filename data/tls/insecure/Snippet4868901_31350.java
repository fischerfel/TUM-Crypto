private static class AnyTrustManager implements X509TrustManager
{
    public void checkClientTrusted(X509Certificate[] chain, String authType)
    {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType)
    {
    }

    public X509Certificate[] getAcceptedIssuers()
    {
        return new X509Certificate[0];
    }
}

static {
    try {
        SSLContext ssl = SSLContext.getInstance("SSL");
        ssl.init(null, new X509TrustManager[] {new AnyTrustManager()}, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(ssl.getSocketFactory());
    } catch (NoSuchAlgorithmException ex) {
        throw new Error(ex);
    } catch (KeyManagementException ex) {
        throw new Error(ex);
    }
}
