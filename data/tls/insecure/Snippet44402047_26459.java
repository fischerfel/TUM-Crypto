public SSLSocket enterSecureMode(Socket s) throws Exception {
        KeyStore truststore = KeyStore.getInstance("JKS");
        truststore.load(Files.newInputStream(Paths.get("truststore.jks")), "mypass".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(truststore);

        SSLContext sCon = SSLContext.getInstance("TLS");
        sCon.init(null, tmf.getTrustManagers(), null);

        SSLSocketFactory sslSocketFactory = sCon.getSocketFactory();
        return (SSLSocket) sslSocketFactory.createSocket(s, "<HOSTNAME>", 21, true);
}
