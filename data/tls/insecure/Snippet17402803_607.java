public CBISKeyStoreAwareMessageSender() {

    try {
        KeyStore ks = KeyStore.getInstance("JKS");
        InputStream is = new FileInputStream("D:\\Dev\\keystore\\my-keystore.jks");
        ks.load(is, "mypass1".toCharArray());
        setSslProtocol("TLSv1");
        SSLContext c = SSLContext.getInstance("TLSv1");
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        super.setTrustManagers(tmf.getTrustManagers());
        //X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, "cbis1".toCharArray());

        super.setKeyManagers(kmf.getKeyManagers());
        c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        SSLContext.setDefault(c);

    } catch (Exception ex) { 
        // TODO Auto-generated catch block
        ex.printStackTrace();
    }


}
