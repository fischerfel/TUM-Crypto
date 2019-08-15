SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
// The SSL context must be explicitly initialized
sslContext.init(null, null, null);
ResteasyClient restClient = new ResteasyClientBuilder().sslContext(instance).build();
