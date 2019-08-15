private static TrustManager[] getTrustManager()
{
    X509Certificate[] server = null;
    X509Certificate[] client = null;
    X509TrustManager tm = new X509TrustManager()
    {
        X509Certificate[] server1 = null;
        X509Certificate[] client1 = null;
        public X509Certificate[] getAcceptedIssuers()
        {
            return new X509Certificate[0];
        }

        public void checkServerTrusted(X509Certificate[] chain, String x)
        {
            server1 = chain;
            Logger.println("X509 Certificate chain: " + chain);
        }

        public void checkClientTrusted(X509Certificate[] chain, String x)
        {
            client1 = chain;
            Logger.println("X509 Certificate chain: " + chain);
        }
    };

    return new X509TrustManager[]{tm};
}
