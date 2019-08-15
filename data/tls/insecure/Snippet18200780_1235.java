private void setupSecurity() {
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextInt();

    KeyStore clientKeyStore = KeyStore.getInstance("JKS");
    clientKeyStore.load(new FileInputStream("client.jks"), "KeyStorePassword".toCharArray()); 

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(clientKeyStore);

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(clientKeyStore, "KeyInKeystorePassword".toCharArray());

    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), secureRandom);
}
