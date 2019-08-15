public static void main(String[] args) {
    CallWS callWS = new CallWS();

    try {
        doTrustToCertificates();
        hitHttps();

    } catch (Exception ex) {
        ex.printStackTrace();
    }

}



public static void doTrustToCertificates() throws Exception {
    Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
    TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }



            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificate,
                                           String string) throws CertificateException {
                // TODO Implement this method

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificate,
                                           String string) throws CertificateException {
                // TODO Implement this method

            }
        }
    };

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HostnameVerifier hv = new HostnameVerifier() {
        @Override
        public boolean verify(String urlHostName, SSLSession session) {
            if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                //logger.warn("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
            }
            return true;
        }
    };
    HttpsURLConnection.setDefaultHostnameVerifier(hv);
}



private static void hitHttps() throws Exception {

    // Create SOAP Connection
    SOAPConnectionFactory soapConnectionFactory =
        SOAPConnectionFactory.newInstance();
    SOAPConnection soapConnection =
        soapConnectionFactory.createConnection();

    System.setProperty("javax.net.ssl.keyStore",
                       "C:\\Java\\jdk1.8.0_77\\jre\\lib\\security\\cacerts");
    System.setProperty("javax.net.ssl.keyStorePassword", "changeit");

    System.setProperty("javax.net.ssl.trustStore", "C:\\Java\\jdk1.8.0_77\\jre\\lib\\security\\cacerts");
    System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

    String urlStr =
        "https://targetService:443/InvoiceService";       

    SOAPMessage soapResponse =
        soapConnection.call(createSOAPRequest(), urlStr);


    // print SOAP Response
    System.out.print("Response SOAP Message:");
    soapResponse.writeTo(System.out);

    soapConnection.close();
}
