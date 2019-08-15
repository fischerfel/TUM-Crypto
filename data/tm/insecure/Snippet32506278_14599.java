Client client = trustAllCerts();
clientResource = new ClientResource(url);
clientResource.setNext(client);
try{
      clientText = clientResource.post"");
 }
 catch(ResourceException e){
    e.printStackTrace();
 }

public Client trustAllCerts() {
    Client client = null;
    try {
        client = new Client(new Context(), Protocol.HTTPS);
        Context context = client.getContext();


        final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        context.getAttributes().put("sslContextFactory", new SslContextFactory() {
            public void init(Series<Parameter> parameters) {

            }

            public SSLContext createSslContext() {
                return sslContext;
            }
        });
        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        context.getAttributes().put("hostnameVerifier", new HostnameVerifier() {                
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }

        });         

        sslContext.init(null, new TrustManager[] { tm }, null);         

    } catch (KeyManagementException e) {
        LOGGER.error("Exception in Key Management" + e);
    } catch (NoSuchAlgorithmException e) {
        LOGGER.error("Exception in Algorithm Used" + e);
    }
    return client;
}
