    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs,
                String authType) {

        }

        public void checkServerTrusted(X509Certificate[] certs,
                String authType) {

        }

    } };

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    // Create all-trusting host name verifier
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    // Install the all-trusting host verifier
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    /*
     * end of the fix
     */
    // logger.info(input);
    String responseString = "";
    List<Object> providers = new ArrayList<Object>();
    providers.add(new String());

    WebClient client = WebClient
            .create("https://1.2.3.4:8443/api/methodName");

    WebClient.getConfig(client).getHttpConduit().getClient()
            .setConnectionTimeout(5000);

    System.out.println("input : " + requestString);
    client.header("username", new Object[] { headerUsername });
    client.header("password", new Object[] { headerPassword });
    client.header("Authrorization", new Object[] { headerAuth });
    Response response = client.accept(new String[] { "application/json" })
            .type("application/json").post(requestString);
    if (response.getStatus() != 200) {
        throw new Exception("Failed : HTTP error code : "
                + response.getStatus());
    }
    responseString = IOUtils.toString((InputStream) response.getEntity(),
            "UTF-8");
    System.out.println("response : " + responseString);
