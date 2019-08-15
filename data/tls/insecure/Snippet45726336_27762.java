SSLContext sslContext = SSLContext.getInstance("TLS", BouncyCastleProvider.PROVIDER_NAME);
sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
SSLEngine sslEngine = sslContext.createSSLEngine();
String[] suites = { "TLS_ECDHE_ECDSA_WITH_AES_128_CCM_8" };
sslEngine.setEnabledCipherSuites(suites);
