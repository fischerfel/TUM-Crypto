// Create a trust manager that does not validate certificate chains
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
         SSLContext sc = SSLContext.getInstance("SSL");
         // Create empty HostnameVerifier
         HostnameVerifier hv = new HostnameVerifier() {
             public boolean verify(String arg0, SSLSession arg1) {
                 return true;
             }
         };

         sc.init(null, trustAllCerts, new java.security.SecureRandom());
         HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
         HttpsURLConnection.setDefaultHostnameVerifier(hv);  

        Service w_service = new Service();
        Call w_call = (Call)w_service.createCall();
        w_call.setTargetEndpointAddress("https://localhost:8443/webserviceApp/services/ServiceName");
        QName w_qname = new QName("QNAME");
        w_call.setOperationName(w_qname);

        Object[] w_inputArr = new Object[2];

        w_inputArr[0] = "param1";
        w_inputArr[1] = "param2";

  System.out.println((String)w_call.invoke(w_inputArr));
