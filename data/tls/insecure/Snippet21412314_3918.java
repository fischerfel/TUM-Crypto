SslContextFactory sslContextFactory = new SslContextFactory();
sslContextFactory.setNeedClientAuth(true);

TrustManager[] trustManagers = new TrustManager[
        ]{new X509TrustManager() {
    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
        LoggerFactory.getLogger().fatal("Check client trusted");
    }

    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
        LoggerFactory.getLogger().fatal("Check server trusted");
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        LoggerFactory.getLogger().fatal("Returning accepted issuers");

        try {
            InputStream inStream = new FileInputStream("/home/sk_/client.cer");
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
            inStream.close();

            return new X509Certificate[]{cert};
        } catch (Exception e) {
            e.printStackTrace();
            LoggerFactory.getLogger().fatal("AFDASDFSAASD", e);

            return null;
        }
    }
}};

SSLContext context = SSLContext.getInstance("TLSv1.1");
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
FileInputStream fin = new FileInputStream(".hive/certs/mjolnirr.jks");
KeyStore ks = KeyStore.getInstance("JKS");
ks.load(fin, "passwd".toCharArray());

kmf.init(ks, "passwd".toCharArray());
context.init(kmf.getKeyManagers(), trustManagers, null);

SSLEngine engine = context.createSSLEngine();
engine.setNeedClientAuth(true);
engine.setEnabledCipherSuites(engine.getSupportedCipherSuites());
engine.setEnabledProtocols(engine.getSupportedProtocols());

sslContextFactory.setSslContext(context);
