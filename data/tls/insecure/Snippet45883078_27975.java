private SSLContext createSSLContext(){
    try{
        byte[] der = SERVER_CERT.getBytes();
        ByteArrayInputStream derInputStream = new ByteArrayInputStream(der);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(derInputStream);
        String alias = cert.getSubjectX500Principal().getName();

        // Create keystore and add to ssl context
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);
        trustStore.setCertificateEntry(alias, cert);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(trustStore, null);
        KeyManager[] keyManagers = kmf.getKeyManagers();

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(trustStore);
        TrustManager[] trustManagers = tmf.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLSv1.1");
        sslContext.init(keyManagers, trustManagers, null);

        return sslContext;
    } catch (Exception ex){
        ex.printStackTrace();
    }

    return null;
}
