    public String iStream_to_String(InputStream is)
{
    BufferedReader rd = new BufferedReader(new InputStreamReader(is), 4096);
    String line;
    StringBuilder sb = new StringBuilder();
    try
    {
        while ((line = rd.readLine()) != null)
        {
            sb.append(line);
        }
        rd.close();

    } catch (IOException e)
    {
        e.printStackTrace();
    }
    String contentOfMyInputStream = sb.toString();
    return contentOfMyInputStream;
}

final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier()
{
    public boolean verify(String hostname, SSLSession session)
    {
        return true;
    }
};

/**
 * Trust every server - dont check for any certificate
 */
private static void trustAllHosts()
{
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[]
    { new X509TrustManager()
    {
        public java.security.cert.X509Certificate[] getAcceptedIssuers()
        {
            return new java.security.cert.X509Certificate[]
            {};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                String authType) throws CertificateException
        {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                String authType) throws CertificateException
        {
        }
    } };

    // Install the all-trusting trust manager
    try
    {
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection
                .setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e)
    {
        e.printStackTrace();
    }
}
