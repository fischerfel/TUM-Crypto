public static SSLSocketFactory acceptMyCertificate(String sslCert) {
final char[] JKS_PASSWORD = "changeit".toCharArray();
final char[] KEY_PASSWORD = "changeit".toCharArray();
System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
    try {
       final KeyStore keyStore = KeyStore.getInstance("JKS");             
       String workingDir = System.getProperty("user.dir");
       final String path = workingDir+sslCert;
       final InputStream is = new FileInputStream(path);
       keyStore.load(is, JKS_PASSWORD);
       final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
       kmf.init(keyStore, KEY_PASSWORD);
       final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
       tmf.init(keyStore);
       final SSLContext sc = SSLContext.getInstance("TLS");
       sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());
       final SSLSocketFactory socketFactory = sc.getSocketFactory();
       return socketFactory;
    } catch (GeneralSecurityException exc) {
        throw new RuntimeException(exc);
    }
    catch (IOException exc) {
        throw new RuntimeException(exc);
    }
}
