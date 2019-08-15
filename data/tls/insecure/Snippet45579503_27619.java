@Configuration
public class AppConfig {


@Bean(name="awsiotsslcontext")
SSLContext awsiotsslcontext(){

    SSLContext sslContext = null;
    try{
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream("/cert/myApp.cert.pem");
        // You could get a resource as a stream instead.

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate caCert = (X509Certificate)cf.generateCertificate(is);

        TrustManagerFactory tmf = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

        ks.load(null); // You don't need the KeyStore instance to come from a file.
        ks.setCertificateEntry("caCert", caCert);

        tmf.init(ks);

        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
    } catch (Exception e){

    }
    return sslContext;
}
}
