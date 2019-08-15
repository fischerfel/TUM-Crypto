class SSLPinning {

void exec() {

    // Open InputStreams for each certificate
    InputStream baltimoreInputStream = getClass().getResourceAsStream("baltimore.cer");
    InputStream hcpmsInputStream = getClass().getResourceAsStream("hcpms_cert.cer");
    InputStream verizonInputStream = getClass().getResourceAsStream("verizon.cer");

    try {

        // CertificateFactory has the method that generates certificates from InputStream
        // Default type for getInstance is X.509
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        // Create Certificate objects for each certificate
        Certificate baltimoreCertificate = cf.generateCertificate(baltimoreInputStream);
        Certificate hcpmsCertificate = cf.generateCertificate(hcpmsInputStream);
        Certificate verizonCertificate = cf.generateCertificate(verizonInputStream);

        // Create KeyStore and load it with our certificates
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        //keyStore.setCertificateEntry("hcpms", hcpmsCertificate);
        keyStore.setCertificateEntry("intermediate", verizonCertificate); //surprisingly, it works with just using the intermediate CA
        //keyStore.setCertificateEntry("root", baltimoreCertificate);

        // Create a TrustManagerFactory using KeyStore -- this is responsible in authenticating the servers
        // against our stored certificates
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        // Create an SSLContext using TrustManagerFactory -- this will generate the SSLSocketFactory we will use
        // during HTTPS connection
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        URL url = new URL("https://account.hanatrial.ondemand.com/");
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)url.openConnection();
        httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
        httpsURLConnection.connect();
        System.out.print("Server authentication successful");

    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (SSLHandshakeException e) {
        System.out.println("Server authentication failed");
    } catch (IOException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }

}
