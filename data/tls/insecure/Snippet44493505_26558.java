KeyStore clientStore  = KeyStore.getInstance("PKCS12"); 
InputStream instream = Thread.currentThread().getContextClassLoader().getResourceAsStream(keystoreName);
try {
    clientStore.load(instream, keyStorePwd.toCharArray());
} finally {
    instream.close();
}
//Trust everybody
X509TrustManager tm = new X509TrustManager() {
    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {}
    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {}
    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {return null;}
};
SSLContext sslCtx = SSLContext.getInstance("TLS");
KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmfactory.init(clientStore, keyStorePwd != null ? keyStorePwd.toCharArray() : null);
KeyManager[] keymanagers = kmfactory.getKeyManagers();
sslCtx.init(keymanagers, new TrustManager[]{tm}, null);
SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslCtx);
Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslConnectionFactory).register("http", new PlainConnectionSocketFactory()).build();
PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(registry);
HttpClientBuilder hcb = HttpClientBuilder.create();
hcb.setConnectionManager(pcm);
CloseableHttpClient httpClient = hcb.build();
