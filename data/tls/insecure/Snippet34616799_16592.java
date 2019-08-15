TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager()
{
    @Override
    public X509Certificate[] getAcceptedIssuers(){return null;}
    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType){}
    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType){}
}};

// Install the all-trusting trust manager
SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, trustAllCerts, new SecureRandom());
ftpsClient.setSSLSocketFactory(sc.getSocketFactory());
