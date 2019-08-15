int PORT = 12345;
File KEYSTORE = new File("path/to/my.keystore");
String PASS = "mykeystorepass";

HttpsServer server = HttpsServer.create(new InetSocketAddress(PORT), 0);

SSLContext sslContext = SSLContext.getInstance("TLS");
char[] password = PASS.toCharArray();
KeyStore ks = KeyStore.getInstance("JKS");
FileInputStream fis = new FileInputStream(KEYSTORE);
ks.load(fis, password);
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(ks, password);

TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
tmf.init(ks);

sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
    public void configure(HttpsParameters params) {
        try {
            SSLContext c = SSLContext.getDefault();
            SSLEngine engine = c.createSSLEngine();
            params.setNeedClientAuth(false);
            params.setCipherSuites(engine.getEnabledCipherSuites());
            params.setProtocols(engine.getEnabledProtocols());
            SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
            params.setSSLParameters(defaultSSLParameters);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
});

server.createContext("/", new Handler());
server.setExecutor(null);
server.start();
