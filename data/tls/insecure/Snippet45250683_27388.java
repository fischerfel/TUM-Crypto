    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
        public X509Certificate[] getAcceptedIssuers(){return null;}
        public void checkClientTrusted(X509Certificate[] certs, String authType){}
        public void checkServerTrusted(X509Certificate[] certs, String authType){}
    }};

    // Install the all-trusting trust manager
    RestTemplate restTemplate = new RestTemplate();

    try {

        String keyPassphrase = "changeit";

        KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(new FileInputStream("c:\\jks\\client.jks"), keyPassphrase.toCharArray());

        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());           
        kmfactory.init(keyStore, keyPassphrase.toCharArray());

        KeyManager[] keyManager = kmfactory.getKeyManagers();

        SSLContext sc = SSLContext.getInstance("TLS");

        sc.init(keyManager, trustAllCerts, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier(){

            public boolean verify(String hostname,
                    javax.net.ssl.SSLSession sslSession) {
                if (hostname.equals("localhost")) {
                    return true;
                }
                return false;
            }
        });

    } catch (Exception e) {
        ;
    }       




    System.out.println("Testing listAllUsers API-----------");


    restTemplate.setErrorHandler(new ResponseErrorHandler(){

        @Override
        public void handleError(ClientHttpResponse rs) throws IOException {
            InputStream in = rs.getBody();
            String str = new String("");
            int len =0;
            while((len = in.available()) > 0){
                byte[] bytes = new byte[len];
                in.read(bytes);
               str = str + new String (bytes, "UTF-8");

            }

            System.out.println(str);

        }

        @Override
        public boolean hasError(ClientHttpResponse rs) throws IOException {

            return false;
        }

    });
    try{

    String usersMap = restTemplate.getForObject(REST_SERVICE_URI+"/user/shailendra/", String.class);`
