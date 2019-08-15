class TrustEveryoneManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
OkHttpClient client = new OkHttpClient();

final InetAddress ipAddress = InetAddress.getByName("XX.XXX.XXX.XXX"); // some IP
client.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ipAddress, 8888)));

SSLContext sslContext = SSLContext.getInstance("TLS");
TrustManager[] trustManagers = new TrustManager[]{new TrustEveryoneManager()};
sslContext.init(null, trustManagers, null);
client.setSslSocketFactory(sslContext.getSocketFactory);
