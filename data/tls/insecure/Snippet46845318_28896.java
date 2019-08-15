static public void doTrustToCertificates() throws Exception 
{

    // Set truststore that contains root / intermediary certs
    System.setProperty("javax.net.ssl.trustStore", "C:\\cert\\trusted.jks");
    System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

    // Set keystore that contains private key
    File pKeyFile = new File("C:\\cert\\privatekey.pfx");
    String pKeyPassword = "Password01";
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    InputStream keyInput = new FileInputStream(pKeyFile);
    keyStore.load(keyInput, pKeyPassword.toCharArray());
    keyInput.close();
    keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

    // Set ssl context with private key and truststore details
    SSLContext sc = SSLContext.getInstance("TLSv1");
    sc.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
    SSLSocketFactory sockFact = sc.getSocketFactory();

    // Add ssl context to https connection
    HttpsURLConnection.setDefaultSSLSocketFactory(sockFact);

}
