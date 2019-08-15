    public void start(String a_sAddress, int a_nPort) throws IOException {

    try {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain,  String authType) {
                // TODO Auto-generated method stub

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                // TODO Auto-generated method stub

            }
        } };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );

    //  Init a configuration with our SSL context
        HttpsConfigurator configurator = new HttpsConfigurator(sslContext);

       HttpsServer server = HttpsServer.create(new InetSocketAddress(a_sAddress, a_nPort), 0);

       server.setHttpsConfigurator(configurator);

       //here - attaching HttpHanlder code.

       server.setExecutor(null); // creates a default executor
       server.start();
    }
    catch (KeyManagementException e) {
        System.out.println("HttpsRequest - setTLSMode - KeyManagementException");
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        System.out.println("HttpsRequest - setTLSMode - NoSuchAlgorithmException");
        e.printStackTrace();
    }
}
