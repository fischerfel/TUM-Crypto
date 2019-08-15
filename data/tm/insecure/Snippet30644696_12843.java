public IExample create() throws MalformedURLException{
        try{
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    // Trust always
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    // Trust always
                }

            } };
            // Install the all-trusting trust manager
             sc = SSLContext.getInstance("SSL");
            // Create empty HostnameVerifier
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession arg1) {               

                        if(hostname.equals(arg1.getPeerHost()) && hostname.equals("example.com"))
                        {
                            return true;
                        }else{ 
                            return false;
                        }
                    }
                };

            sc.init(null,trustAllCerts , new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
         }
         catch(Exception e){
            e.printStackTrace();
         }

        try{
            URL url = new URL( urlString );

            //1st argument service URI, refer to wsdl document above
            //2nd argument is service name, refer to wsdl document above
                QName qname = new QName("http://synchronization.ws/", "ExampleImplclass");

                Service service = Service.create(url, qname);
                IExample iExample = service.getPort(IExample.class);       
                return iExample;        
        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
