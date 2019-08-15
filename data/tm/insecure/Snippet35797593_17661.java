private static TrustManager[] trustManagers()
{
    X509TrustManager tm = new X509TrustManager()
    {
        public X509Certificate[] getAcceptedIssuers()
        {
            return new X509Certificate[0];
        }

        public void checkServerTrusted(X509Certificate[] chain, String x)
        {
            // Dummy method
        }

        public void checkClientTrusted(X509Certificate[] chain, String x)
        {
            // Dummy method
        }
    };

    return new X509TrustManager[]{tm};
}
