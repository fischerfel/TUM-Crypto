RemoteWSCredentials cred = new RemoteWSCredentials();
cred.setUserid("username");
cred.setPassword("password");
URL url = new URL("https://hostname/webservicelocation"); //the exposed url
System.setProperty("https.proxyHost", "ipHostnameProxy"); //set proxy properties
System.setProperty("https.proxyPort", "portProxy");
    try {
        SSLContext sslContext = SSLContext.getInstance("SSL"); //instance SSLContext

        // set up a TrustManager that trusts everything
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {

                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs,
                        String authType) {

                }

                public void checkServerTrusted(X509Certificate[] certs,
                        String authType) {                        

                }
            }}, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(
                sslContext.getSocketFactory());

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {

                return true;
            }
        });     
  } catch (KeyManagementException ex) {
         System.err.println("KeyManagementException: " + ex.getMessage());
    } catch (NoSuchAlgorithmException ex) {
         System.err.println("NoSuchAlgorithmException: " + ex.getMessage());
    }

RemoteWSService ws = new RemoteWSService(url);  //instance WS Service 
boolean result =   ws.exposedService(cred);
