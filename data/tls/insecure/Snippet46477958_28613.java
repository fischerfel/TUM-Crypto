HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.USER_AGENT, "YOURS/1.0.0");
    headers.set("X-App-Username", "YOURS");
    headers.set("App-Username", "YOURS");
    headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
    headers.set(HttpHeaders.CONNECTION, "Keep-Alive");
    headers.set(HttpHeaders.HOST, "IP");
    headers.set(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR");
    headers.add("Accept","application/json;charset=UTF-8");
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    request = new HttpEntity<HttpHeaders>(headers);
    System.setProperty("javax.net.ssl.trustStore", "YOUR_PATH/clientcert.jks");
    System.setProperty("javax.net.ssl.trustStorePassword", "pwd123");
    System.setProperty("javax.net.ssl.trustStoreType", "JKS");
    System.setProperty("javax.net.ssl.keyStore", "YOUR_PATH/trustStore.jks");
    System.setProperty("javax.net.ssl.keyStorePassword", "pwd123");
    System.setProperty("javax.net.ssl.keyStoreType", "JKS");  
    System.setProperty("javax.net.ssl.keyAlias", "localhost");
    System.setProperty("javax.net.ssl.enabled", "true");
    System.setProperty("javax.net.ssl.defaul-type", "JKS");
    System.setProperty("javax.net.ssl.client-auth", "need");
    System.setProperty("javax.net.ssl.protocols", "TLSv1.2");

    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) { }
        public void checkServerTrusted(X509Certificate[] certs, String authType) { }
    } };

    SSLContext sc = null;
    try {
        sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
    } catch (KeyManagementException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };      
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(null, headers);
    responseEntity = null;

    mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
