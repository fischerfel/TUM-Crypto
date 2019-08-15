String pwd = "mypwd";
InputStream keyStoreUrl = new FileInputStream("client.p12");
InputStream trustStoreUrl = new FileInputStream("server.jks");

KeyStore keyStore = KeyStore.getInstance("PKCS12");
keyStore.load(keyStoreUrl, pwd.toCharArray());
KeyManagerFactory keyManagerFactory = 
    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyManagerFactory.init(keyStore, pwd.toCharArray());

KeyStore trustStore = KeyStore.getInstance("JKS");
trustStore.load(trustStoreUrl, pwd.toCharArray());
TrustManagerFactory trustManagerFactory = 
    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(trustStore);

final SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(keyManagerFactory.getKeyManagers(), 
                trustManagerFactory.getTrustManagers(), 
                new SecureRandom());
SSLContext.setDefault(sslContext);
