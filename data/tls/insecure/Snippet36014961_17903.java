public SSLContext getSSLContext() {

        String kmfAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(kmfAlgorithm);
        kmf.init(this.trustStore, this.truststorePassword.toCharArray());

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(this.trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");      
        sslContext.init(null, tmf.getTrustManagers(), null);
}
