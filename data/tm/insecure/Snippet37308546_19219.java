@Test
public void GetMember2(){
    try{
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

        Date today1 = new Date(); // default window is 1 hour
        String stringToday1 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).format(today1);

        Client client = Client.create(configureClient());

        WebResource webResource = client.resource("https://test.abcd.net/Members/72771583886/promotions?startDate=12122015");

        ClientResponse response = webResource.accept("application/xml")
                .type("application/xml")
                .header("Date", stringToday1)
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
           throw new RuntimeException("Failed : HTTP error code : "
            + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("Output from Server .... \n");
        System.out.println(output);

      } catch (Exception e) {
        e.printStackTrace();
      }
}

public static ClientConfig configureClient() {
    TrustManager[ ] certs = new TrustManager[ ] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
            }
    };
    SSLContext ctx = null;
    try {
        ctx = SSLContext.getInstance("TLS");
        ctx.init(null, certs, new SecureRandom());
    } catch (java.security.GeneralSecurityException ex) {
    }
    HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

    ClientConfig config = new DefaultClientConfig();
    try {
        config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(
            new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }, 
            ctx
        ));
    } catch(Exception e) {
    }

    return config;
}
