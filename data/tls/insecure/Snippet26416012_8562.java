public class MYClass {
private SSLSocketFactory sslSocketFactory;   
public MyClass() throws IOException{
    String trustStoreFilePath = System.getProperty("javax.net.ssl.trustStore");
    try {
            TrustManagerFactory trustManagerFactory = CertManagerFactory.loadTrustStore(trustStoreFilePath);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            this.sslSocketFactory = sslContext.getSocketFactory();
        } 
        catch (NoSuchAlgorithmException e) {} 
        catch (KeyManagementException e) {}
    }
}
