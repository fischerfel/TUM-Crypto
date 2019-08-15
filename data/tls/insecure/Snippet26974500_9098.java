String url = ConfigMessages.getString("url")+"ArWorkSchedular/Data"; 
     try{

         TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
                public X509Certificate[] getAcceptedIssuers(){return null;}
                public void checkClientTrusted(X509Certificate[] certs, String authType){}
                public void checkServerTrusted(X509Certificate[] certs, String authType){}
            }};
            // Install the all-trusting trust manager
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        // TODO Auto-generated method stub
                        return true;
                    }
                });
            } catch (Exception e) {
                ;
            }
         Client client = Client.create();
            WebResource webResource = client.resource(url);
            ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).header("username", "UNAME").header("password", "PASS").post(ClientResponse.class);  
            if (response.getStatus() != 200) {
               throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
            }
            String output = response.getEntity(String.class);
