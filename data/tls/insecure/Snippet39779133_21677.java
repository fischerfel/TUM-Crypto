KeyStore ks = KeyStore.getInstance("BKS");
InputStream in = new ByteArrayInputStream(<byte [] my_ssl_data>); 
ks.load(in, <string mypassword>.toCharArray());
in.close();

TrustManagerFactory Main_TMF = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
Main_TMF.init(ks);

X509TrustManager Cur_Trust_Manager = new X509TrustManager()
{
    public void checkClientTrusted(X509Certificate [] chain, String authType) throws CertificateException { }
    public void checkServerTrusted(X509Certificate [] chain, String authType) throws CertificateException { }
    public X509Certificate [] getAcceptedIssuers() { return null; }
};

SSLContext sslContext =  SSLContext.getInstance("TLS");
sslContext.init(null, new TrustManager[] { Cur_Trust_Manager }, new SecureRandom());

HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
{
    @Override
    public boolean verify(String hostname, SSLSession session)
    {
        try
        {
            Cur_Trust_Manager.checkServerTrusted((X509Certificate []) session.getPeerCertificates(), session.getCipherSuite());
            return true;
        }
        catch (Exception e) {}
        return false;
    }
});
