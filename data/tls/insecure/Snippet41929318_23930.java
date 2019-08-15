public class NewSSLSocketFactory extends SSLSocketFactory {
    private SSLContext sslContext=SSLContext.getInstance("TLS");  
    private KeyStore setCert(){
        String skuCertBin ="-----BEGIN CERTIFICATE-----\n"+
                                             "SOMETHING TEXT HERE"+
                                      "-----END CERTIFICATE-----";  
        KeyStore trustStore=null;  
        try {
            ByteArrayInputStream derInputStream = new ByteArrayInputStream(skuCertBin.getBytes());  
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");  
            X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(derInputStream);  
            String alias = cert.getSubjectX500Principal().getName();  
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
            trustStore.load(null, null);  
            trustStore.setCertificateEntry(alias, cert);
        } catch (CertificateException e) {
            e.printStackTrace();  
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  
        } catch (KeyStoreException e) {
            e.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();  
        }
        return trustStore;  
    }
    public NewSSLSocketFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        super();  
        KeyStore trustStore = setCert();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");  
        tmf.init(trustStore);  
        sslContext.init(null, tmf.getTrustManagers(), null);
    }
....................
}
